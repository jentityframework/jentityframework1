package jentityframework.lamdasql;

import com.trigersoft.jaque.expression.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.trigersoft.jaque.expression.ExpressionType.*;

public class ToSqlVisitor implements ExpressionVisitor<StringBuilder> {

	private StringBuilder sb = new StringBuilder();
	private Expression body;
	private List<ConstantExpression> parameters = new ArrayList<>();

	private static String toSqlOp(int expressionType) {
		switch (expressionType) {
		case Equal:
			return "=";
		case LogicalAnd:
			return "AND";
		case LogicalOr:
			return "OR";
		case Convert:
			return "";
		}
		return ExpressionType.toString(expressionType);
	}

	@Override
	public StringBuilder visit(BinaryExpression e) {
		boolean quote = e != body && e.getExpressionType() == LogicalOr;

		if (quote)
			sb.append('(');

		e.getFirst().accept(this);
		sb.append(' ').append(toSqlOp(e.getExpressionType())).append(' ');
		e.getSecond().accept(this);

		if (quote)
			sb.append(')');

		return sb;
	}

	@Override
	public StringBuilder visit(ConstantExpression e) {
		if (e.getValue() instanceof String) {
			return sb.append("'").append(e.getValue().toString()).append("'");
		}
		return sb.append(e.getValue().toString());
	}

	@Override
	public StringBuilder visit(InvocationExpression e) {
		e.getArguments().stream().filter(x -> x instanceof ConstantExpression)
				.forEach(x -> parameters.add((ConstantExpression) x));
		return e.getTarget().accept(this);
	}

	@Override
	public StringBuilder visit(LambdaExpression<?> e) {
		this.body = e.getBody();
		return body.accept(this);
	}

	@Override
	public StringBuilder visit(MemberExpression e) {
		String name = "";
		if (e.toString().contains("isBefore")) {
			Pattern pattern = Pattern.compile(".{4},\\s+.{1,2},\\s+.{1,2}");
			Matcher matcher = pattern.matcher(body.toString());
			matcher.find();
			String date = "'" + matcher.group().replace(", ", "-") + "'";
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " < " + date + " ";
		} else if (e.toString().contains("isAfter")) {
			Pattern pattern = Pattern.compile(".{4},\\s+.{1,2},\\s+.{1,2}");
			Matcher matcher = pattern.matcher(body.toString());
			matcher.find();
			String date = "'" + matcher.group().replace(", ", "-") + "'";
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " > " + date + " ";
		} else if (body.toString().contains("java.time.LocalDate") && body.toString().contains("equals")) {
			Pattern pattern = Pattern.compile(".{4},\\s+.{1,2},\\s+.{1,2}");
			Matcher matcher = pattern.matcher(body.toString());
			matcher.find();
			String date = "'" + matcher.group().replace(", ", "-") + "'";
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " = " + date + " ";
		}

		else if (e.toString().contains("getYear")) {
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = " year(" + name + ") ";
		} else if (e.toString().contains("getMonthValue")) {
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = " month(" + name + ") ";
		} else if (e.toString().contains("getDayOfMonth")) {
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = " day(" + name + ") ";
		} else if (e.toString().contains("compareTo")) {
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
		} else if (e.getMember().getName().contains("contains")) {
			String keyword = body.toString().split("\\.")[0];
			keyword = keyword.replace("(", "");
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " like '%" + keyword + "%'";
		} else if (e.getMember().getName().contains("startsWith")) {
			String keyword = body.toString().split("\\.")[0];
			keyword = keyword.replace("(", "");
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " like '" + keyword + "%'";
		} else if (e.getMember().getName().contains("endsWith")) {
			String keyword = body.toString().split("\\.")[0];
			keyword = keyword.replace("(", "");
			String[] s = e.toString().split("\\.");
			name = s[1].replaceAll("^(get)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			name = name.replace("(", "").replace(")", "");
			name = name + " like '%" + keyword + "'";
		} else {
			name = e.getMember().getName();
			name = name.replaceAll("^(get)", "");
			name = name.replaceAll("^(is)", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		return sb.append(name);
	}

	@Override
	public StringBuilder visit(ParameterExpression e) {
		parameters.get(e.getIndex()).accept(this);
		return sb;
	}

	@Override
	public StringBuilder visit(UnaryExpression e) {
		sb.append(toSqlOp(e.getExpressionType()));
		return e.getFirst().accept(this);
	}

}
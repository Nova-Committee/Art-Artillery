package committee.nova.plr.aa2.common.tool.maths;

import java.text.MessageFormat;

public class FormatUtils {
    public static String formattedNumber(float raw, int amount) {
        final String format = raw > 10.0F ? "%.0f" : MessageFormat.format("%.{0}f", amount);
        return String.format(format, raw);
    }
}

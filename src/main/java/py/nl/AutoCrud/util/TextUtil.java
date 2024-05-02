package py.nl.AutoCrud.util;

public class TextUtil {
	public static String generateLabel(String text) {
		String labelText = null;
		String[] parts = text.split("(?=\\p{Upper})");
		for (int j = 0; j < parts.length; j++) {
			if (j == 0)
				labelText = parts[j].toUpperCase();
			else
				labelText = labelText + " " + parts[j].toUpperCase();
		}
		return labelText;
	}
}

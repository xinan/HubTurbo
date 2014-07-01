package util;

import java.util.Collections;
import java.util.List;

public class UserConfigurations {
	private static List<String> openStatuses;
	public List<String> getOpenStatuses() {
		return Collections.unmodifiableList(openStatuses);
	}
	
	private static List<String> excludedLabels;
	public List<String> getExcludedLabels() {
		return Collections.unmodifiableList(excludedLabels);
	}
	
	public UserConfigurations(List<String> openStatuses, List<String> excludedLabels) {
		UserConfigurations.openStatuses = openStatuses;
		UserConfigurations.excludedLabels = excludedLabels;
	}

	public static boolean isOpenStatus(String status) {
		return openStatuses.contains(status);
	}
	
	public static boolean isExcludedLabel(String label) {
		return excludedLabels.contains(label);
	}
	
}

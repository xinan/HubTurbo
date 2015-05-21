package backend.resource;


import backend.resource.serialization.SerializableLabel;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import org.eclipse.egit.github.core.Label;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class TurboLabel {

	private static final String EXCLUSIVE_DELIMITER = ".";
	private static final String NONEXCLUSIVE_DELIMITER = "-";

	private void ______SERIALIZED_FIELDS______() {
	}

	private final String actualName;
	private final String colour;

	private void ______TRANSIENT_FIELDS______() {
	}

	private final String repoId;

	private void ______CONSTRUCTORS______() {
	}

	public TurboLabel(String repoId, String name) {
		this.actualName = name;
		this.colour = "#ffffff";
		this.repoId = repoId;
	}

	public TurboLabel(String repoId, String group, String name) {
		this.actualName = join(group, name);
		this.colour = "#ffffff";
		this.repoId = repoId;
	}

	public TurboLabel(String repoId, Label label) {
		this.actualName = label.getName();
		this.colour = label.getColor();
		this.repoId = repoId;
	}

	public TurboLabel(String repoId, SerializableLabel label) {
		this.actualName = label.getActualName();
		this.colour = label.getColour();
		this.repoId = repoId;
	}

	private void ______METHODS______() {
	}

	private Optional<String> getDelimiter() {

		// Escaping due to constants not being valid regexes
		Pattern p = Pattern.compile(String.format("^[^\\%s\\%s]+(\\%s|\\%s)",
			EXCLUSIVE_DELIMITER,
			NONEXCLUSIVE_DELIMITER,
			EXCLUSIVE_DELIMITER,
			NONEXCLUSIVE_DELIMITER));
		Matcher m = p.matcher(actualName);

		if (m.find()) {
			return Optional.of(m.group(1));
		} else {
			return Optional.empty();
		}
	}

	private String join(String group, String name) {
		return group + getDelimiter() + name;
	}

	public boolean isExclusive() {
		if (getDelimiter().isPresent()) {
			return getDelimiter().get().equals(EXCLUSIVE_DELIMITER);
		} else {
			return false;
		}
	}

	public Optional<String> getGroup() {
		if (getDelimiter().isPresent()) {
			// Escaping due to constants not being valid regexes
			return Optional.of(actualName.split("\\" + getDelimiter().get())[0]);
		} else {
			return Optional.empty();
		}
	}

	public String getName() {
		if (getDelimiter().isPresent()) {
			// Escaping due to constants not being valid regexes
			return actualName.split("\\" + getDelimiter().get())[1];
		} else {
			return actualName;
		}
	}

	private String getStyle() {
		String colour = getColour();
		int R = Integer.parseInt(colour.substring(0, 2), 16);
		int G = Integer.parseInt(colour.substring(2, 4), 16);
		int B = Integer.parseInt(colour.substring(4, 6), 16);
		double L = 0.2126 * R + 0.7152 * G + 0.0722 * B;
		boolean bright = L > 128;
		return "-fx-background-color: #" + getColour() + "; -fx-text-fill: " + (bright ? "black" : "white");
	}

	public Node getNode() {
		javafx.scene.control.Label node = new javafx.scene.control.Label(getName());
		node.getStyleClass().add("labels");
		node.setStyle(getStyle());
		if (getGroup().isPresent()) {
			Tooltip groupTooltip = new Tooltip(getGroup().get());
			node.setTooltip(groupTooltip);
		}
		return node;
	}

	private void ______BOILERPLATE______() {
	}

	public String getRepoId() {
		return repoId;
	}
	public String getColour() {
		return colour;
	}
	public String getActualName() {
		return actualName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TurboLabel that = (TurboLabel) o;

		if (!actualName.equals(that.actualName)) return false;
		if (!colour.equals(that.colour)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = actualName.hashCode();
		result = 31 * result + colour.hashCode();
		return result;
	}
}
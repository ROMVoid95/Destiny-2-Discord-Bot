package site.romvoid.forgebot.util.destiny.raids;

/**
 * @author ROMVoid
 *
 */
public class Raids {
	String[] normalHashes;
	String[] prestigeHashes;
	String[] guidedHashes;
	int normalCompletions;
	int prestigeCompletions;
	int guidedCompletions;

	/**
	 * @param normalHashes
	 * @param prestigeHashes
	 * @param guidedHashes
	 * @param normalCompletions
	 * @param prestigeCompletion
	 * @param guidedCompletions
	 */
	public Raids(String[] normalHashes, String[] prestigeHashes, String[] guidedHashes, int normalCompletions, int prestigeCompletion, int guidedCompletions) {
		super();
		this.normalHashes = normalHashes;
		this.prestigeHashes = prestigeHashes;
		this.guidedHashes = guidedHashes;
		this.normalCompletions = normalCompletions;
		this.prestigeCompletions = prestigeCompletion;
		this.guidedCompletions = guidedCompletions;
	}

	/**
	 * @param normalHashes
	 * @param prestigeHashes
	 * @param guidedHashes
	 */
	public Raids(String[] normalHashes, String[] prestigeHashes, String[] guidedHashes) {
		this(normalHashes, prestigeHashes, guidedHashes, 0, 0, 0);
	}

	public Raids() {
		this(null, null, null, 0, 0, 0);
	}

	/**
	 * @param completions
	 */
	public void addNormalCompletions(int completions) {
		normalCompletions += completions;
	}

	public void addPrestigeCompletions(int completions) {
		prestigeCompletions += completions;
	}
	
	public void addGuidedCompletions(int completions) {
		guidedCompletions += completions;
	}

	public int getCompletions() {
		return normalCompletions + prestigeCompletions + guidedCompletions;
	}

	public String[] getNormalHashes() {
		return normalHashes;
	}

	public String[] getPrestigeHashes() {
		return prestigeHashes;
	}
	
	public String[] getGuidedHashes() {
		return guidedHashes;
	}

	public int getNormalCompletions() {
		return normalCompletions;
	}

	public int getPrestigeCompletions() {
		return prestigeCompletions;
	}
	
	public int getGuidedCompletions() {
		return guidedCompletions;
	}

	public void setNormalHashes(String[] normalHashes) {
		this.normalHashes = normalHashes;
	}

	public void setPrestigeHashes(String[] prestigeHashes) {
		this.prestigeHashes = prestigeHashes;
	}
	
	public void setGuidedHashes(String[] guidedHashes) {
		this.guidedHashes = guidedHashes;
	}
	
	public void setNormalCompletions(int normalCompletions) {
		this.normalCompletions = normalCompletions;
	}

	public void setPrestigeCompletions(int prestigeCompletions) {
		this.prestigeCompletions = prestigeCompletions;
	}
	
	public void setGuidedCompletions(int guidedCompletions) {
		this.guidedCompletions = guidedCompletions;
	}


}

package com.prime.util.destiny.raids;

/**
 * @author ROMVoid
 *
 */
public class RaidStats {
	Raids eow;
	Raids sos;
	Raids lev;
	Raids lw;
	Raids sp;

	/**
	 * 
	 */
	public RaidStats() {
		lev = new Raids();
		eow = new Raids();
		sos = new Raids();
		lw = new Raids();
		sp = new Raids();
	}

	/**
	 * @param rs
	 */
	public void combineRaidStats(RaidStats rs) {
		lev.addNormalCompletions(rs.getLev().getNormalCompletions());
		lev.addPrestigeCompletions(rs.getLev().getPrestigeCompletions());
		lev.addGuidedCompletions(rs.getLev().getGuidedCompletions());
		
		eow.addNormalCompletions(rs.getEow().getNormalCompletions());
		eow.addPrestigeCompletions(rs.getEow().getPrestigeCompletions());
		eow.addGuidedCompletions(rs.getEow().getGuidedCompletions());
		
		sos.addNormalCompletions(rs.getSos().getNormalCompletions());
		sos.addPrestigeCompletions(rs.getSos().getPrestigeCompletions());
		sos.addGuidedCompletions(rs.getSos().getGuidedCompletions());
		
	    lw.addNormalCompletions(rs.getLw().getNormalCompletions());
	    lw.addGuidedCompletions(rs.getLw().getGuidedCompletions());
	    
		sp.addNormalCompletions(rs.getSp().getNormalCompletions());
		sp.addGuidedCompletions(rs.getSp().getGuidedCompletions());
	}

	/**
	 * @return
	 */
	public int getTotalCompletion() {
		return lev.getCompletions() + eow.getCompletions() + sos.getCompletions() + lw.getCompletions() + sp.getCompletions();
	}

	public Raids getEow() {
		return eow;
	}

	public Raids getSos() {
		return sos;
	}

	public Raids getLev() {
		return lev;
	}
	
	public Raids getLw() {
	        return lw;
	}
	
	public Raids getSp() {
	        return sp;
	}
	
	public void setEow(Raids eow) {
		this.eow = eow;
	}

	public void setSos(Raids sos) {
		this.sos = sos;
	}

	public void setLev(Raids lev) {
		this.lev = lev;
	}
	
	public void setLw(Raids lw) {
	        this.lw = lw;
	}
	
	public void setSp(Raids sp) {
	        this.sp = sp;
	}

	public String printStats() {
		return String.format
		        ("Leviathan Normal: %d\n"
		       + "Leviathan Prestige: %d\n"
		       + "Leviathan Guided: %d"
		       + "\nEater of Worlds Normal: %d\n"
		       + "Eater of Worlds Prestige: %d\n"
		       + "Eater of Worlds Guided: %d"
		       + "\nSpire of Stars Normal: %d\n"
		       + "Spire of Stars Prestige: %d\n"
		       + "Spire of Stars Guided: %d"
		       + "\nLast Wish: %d\n"
		       + "Last Wish Guided: %d"
		       + "\nScourge of the Past: %d"
		       + "Scourge of the Past Guided: %d"
		       + "\nTotal: %d", 
		        lev.getNormalCompletions(), lev.getPrestigeCompletions(), lev.getGuidedCompletions(),
			eow.getNormalCompletions(), eow.getPrestigeCompletions(), eow.getGuidedCompletions(),
			sos.getNormalCompletions(), sos.getPrestigeCompletions(), sos.getGuidedCompletions(),
			lw.getNormalCompletions(), sp.getNormalCompletions(), lw.getGuidedCompletions(),
			getTotalCompletion());
	}
}

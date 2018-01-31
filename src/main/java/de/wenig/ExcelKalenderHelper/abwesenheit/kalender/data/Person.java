package de.wenig.ExcelKalenderHelper.abwesenheit.kalender.data;

public class Person {

	public Person(int id, String name, String vorname, Firma firma, Organisation organisation, Team team) {
		super();
		this.id = id;
		this.name = name;
		this.vorname = vorname;
		this.firma = firma;
		this.organisation = organisation;
		this.team = team;
	}

	private final int id;
	private final String name;
	private final String vorname;
	private final Firma firma;
	private final Organisation organisation;
	private final Team team;

	public String getName() {
		return name;
	}

	public String getVorname() {
		return vorname;
	}

	public Firma getFirma() {
		return firma;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public Team getTeam() {
		return team;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", vorname=" + vorname + ", firma=" + firma + ", organisation=" + organisation
				+ ", team=" + team + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firma == null) ? 0 : firma.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((organisation == null) ? 0 : organisation.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firma == null) {
			if (other.firma != null)
				return false;
		} else if (!firma.equals(other.firma))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organisation == null) {
			if (other.organisation != null)
				return false;
		} else if (!organisation.equals(other.organisation))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}

}

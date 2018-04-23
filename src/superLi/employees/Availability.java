package superLi.employees;

import java.sql.Date;

public class Availability
{
	private Date date;
	private boolean morningShift, noonShift;

	Availability(Date date, boolean morningShift, boolean noonShift)
	{
		this.date=date;
		this.morningShift=morningShift;
		this.noonShift=noonShift;
	}

	public Date getDate()
	{
		return new Date(date.getTime());
	}

	public boolean isMorningShift()
	{
		return morningShift;
	}

	public boolean isNoonShift()
	{
		return noonShift;
	}

	@Override
	public String toString()
	{
		return "{"+
		       "date="+date+
		       ", morningShift="+isMorningShift()+
		       ", noonShift="+isNoonShift()+
		       '}';
	}
}
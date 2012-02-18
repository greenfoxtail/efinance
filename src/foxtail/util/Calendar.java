package foxtail.util;


public class Calendar {
	
	private static final int Cyear = 1900;/* Note that LC1900.1.1 is SC1900.1.31 */
	private static final int Nyear = 150;
	private final int Nmonth = 13;//阴历年中最多有几个月
	private static final Date Birthday = new Date(1986, 3, 27, 0, 3, 0);
	public static final Date SolarFirstDate = new Date(1900, 1, 31, 0, 3, 0);
	public static final Date LunarFirstDate = new Date(1900, 1, 1, 0, 3, 0);
	//庚年戊月甲日甲时
	public static final Date GanFirstDate = new Date(6, 4, 0, 0, 3, 0);
	//子年戊月甲日甲时
	public static final Date ZhiFirstDate = new Date(0, 2, 4, 0, 3, 0);
	
	private int jieAlert;
	
	
	//*********************************************************
    //* 使用bit位记录每年情况
	//*          0~4共5bit 记录春节日份
	//*          5~6共2bit 记录春节月份
	//*          7~19 共13bit 13个月的大小月情况(如果无闰月，最后位无效)，大月为1,小月为0
	//*          20~23 共4bit 记录闰月的月份，如果没有闰月为0
	//*********************************************************
	private static long []yearInfo = {
	    /* encoding:
			b bbbbbbbbbbbb bbbb
	       bit#    	1 111111000000 0000
		        6 543210987654 3210
	    		. ............ ....
	       month#	  000000000111
		        M 123456789012   L
					
	    b_j = 1 for long month, b_j = 0 for short month
	    L is the leap month of the year if 1<=L<=12; NO leap month if L = 0.
	    The leap month (if exists) is long one iff M = 1.
	    */
	    					0x04bd8,	/* 1900 */
	    0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950,	/* 1905 */
	    0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0,	/* 1910 */
	    0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,	/* 1915 */
	    0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970,	/* 1920 */
	    0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54,	/* 1925 */
	    0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,	/* 1930 */
	    0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60,	/* 1935 */
	    0x186e3, 0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0,	/* 1940 */
	    0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0,	/* 1945 */
	    0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,	/* 1950 */
	    0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573,	/* 1955 */
	    0x052d0, 0x0a9a8, 0x0e950, 0x06aa0, 0x0aea6,	/* 1960 */
	    0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,	/* 1965 */
	    0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0,	/* 1970 */
	    0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250,	/* 1975 */
	    0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0,	/* 1980 */
	    0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,	/* 1985 */
	    0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5,	/* 1990 */
	    0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58,	/* 1995 */
	    0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,	/* 2000 */
	    0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0,	/* 2005 */
	    0x0abb7, 0x025d0, 0x092d0, 0x0cab5, 0x0a950,	/* 2010 */
	    0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,	/* 2015 */
	    0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,	/* 2020 */
	    0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6,	/* 2025 */
	    0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, 0x05aa0,	/* 2030 */
	    0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,	/* 2035 */
	    0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0,	/* 2040 */
	    0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0,	/* 2045 */
	    0x0aa50, 0x1b255, 0x06d20, 0x0ada0			/* 2049 */
	};
	
	/*
	  In "4-column" calculation, a "mingli" (fortune-telling) calculation,
	  the beginning of a month is not the first day of the month as in
	  the Lunar Calendar; it is instead governed by "jie2" (festival).
	  Interestingly, in the Solar calendar, a jie always comes around certain
	  day. For example, the jie "li4chun1" (beginning of spring) always comes
	  near Feburary 4 of the Solar Calendar. 

	  Meaning of array fest:
	  Each element, fest[i][j] stores the jie day (in term of the following Solar
	  month) of the lunar i-th year, j-th month.
	  For example, in 1992, fest[92][0] is 4, that means the jie "li4chun1"
	  (beginning of spring) is on Feb. 4, 1992; fest[92][11] is 5, that means
	  the jie of the 12th lunar month is on Jan. 5, 1993.
	*/
	//存储节气信息

	private static byte [][]fest = {
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1900 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1901 */
	{5, 6, 6, 6, 7, 8, 8, 8, 9, 8, 8, 6},	/* 1902 */
	{5, 7, 6, 7, 7, 8, 9, 9, 9, 8, 8, 7},	/* 1903 */
	{5, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1904 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1905 */
	{5, 6, 6, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1906 */
	{5, 7, 6, 7, 7, 8, 9, 9, 9, 8, 8, 7},	/* 1907 */
	{5, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1908 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1909 */
	{5, 6, 6, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1910 */
	{5, 7, 6, 7, 7, 8, 9, 9, 9, 8, 8, 7},	/* 1911 */
	{5, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1912 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1913 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1914 */
	{5, 6, 6, 6, 7, 8, 8, 9, 9, 8, 8, 6},	/* 1915 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1916 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 7, 6},	/* 1917 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1918 */
	{5, 6, 6, 6, 7, 8, 8, 9, 9, 8, 8, 6},	/* 1919 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1920 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 9, 7, 6},	/* 1921 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1922 */
	{5, 6, 6, 6, 7, 8, 8, 9, 9, 8, 8, 6},	/* 1923 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1924 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 7, 6},	/* 1925 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1926 */
	{5, 6, 6, 6, 7, 8, 8, 8, 9, 8, 8, 6},	/* 1927 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1928 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1929 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1930 */
	{5, 6, 6, 6, 7, 8, 8, 8, 9, 8, 8, 6},	/* 1931 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1932 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1933 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1934 */
	{5, 6, 6, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1935 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1936 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1937 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1938 */
	{5, 6, 6, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1939 */
	{5, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1940 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1941 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1942 */
	{5, 6, 6, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1943 */
	{5, 6, 5, 5, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1944 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1945 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1946 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1947 */
	{5, 5, 5, 5, 6, 7, 7, 8, 8, 7, 7, 5},	/* 1948 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1949 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1950 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1951 */
	{5, 5, 5, 5, 6, 7, 7, 8, 8, 7, 7, 5},	/* 1952 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1953 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 7, 6},	/* 1954 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1955 */
	{5, 5, 5, 5, 6, 7, 7, 8, 8, 7, 7, 5},	/* 1956 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1957 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1958 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1959 */
	{5, 5, 5, 5, 6, 7, 7, 7, 8, 7, 7, 5},	/* 1960 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1961 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1962 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1963 */
	{5, 5, 5, 5, 6, 7, 7, 7, 8, 7, 7, 5},	/* 1964 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1965 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1966 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1967 */
	{5, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1968 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1969 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1970 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1971 */
	{5, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1972 */
	{4, 6, 5, 5, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1973 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1974 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1975 */
	{5, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1976 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 1977 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1978 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1979 */
	{5, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1980 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 1981 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1982 */
	{4, 6, 5, 6, 6, 8, 8, 8, 9, 8, 8, 6},	/* 1983 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1984 */
	{5, 5, 5, 5, 5, 8, 7, 7, 8, 7, 7, 5},	/* 1985 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1986 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1987 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1988 */
	{5, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1989 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 1990 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1991 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1992 */
	{5, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1993 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1994 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1995 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1996 */
	{5, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 1997 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 1998 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 1999 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2000 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2001 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 2002 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 2003 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2004 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2005 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2006 */
	{4, 6, 5, 6, 6, 7, 8, 8, 9, 8, 7, 6},	/* 2007 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2008 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2009 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2010 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 2011 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2012 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2013 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2014 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 2015 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2016 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2017 */
	{4, 5, 5, 5, 6, 7, 7, 8, 8, 7, 7, 5},	/* 2018 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 2019 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 7, 5},	/* 2020 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2021 */
	{4, 5, 5, 5, 6, 7, 7, 7, 8, 7, 7, 5},	/* 2022 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 8, 7, 6},	/* 2023 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 6, 5},	/* 2024 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2025 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2026 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 2027 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 6, 5},	/* 2028 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2029 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2030 */
	{4, 6, 5, 6, 6, 7, 8, 8, 8, 7, 7, 6},	/* 2031 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 6, 5},	/* 2032 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2033 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2034 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2035 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 6, 5},	/* 2036 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2037 */
	{4, 5, 5, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2038 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2039 */
	{4, 5, 4, 5, 5, 6, 7, 7, 8, 7, 6, 5},	/* 2040 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2041 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2042 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2043 */
	{4, 5, 4, 5, 5, 6, 7, 7, 7, 7, 6, 5},	/* 2044 */
	{3, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2045 */
	{4, 5, 4, 5, 5, 7, 7, 7, 8, 7, 7, 5},	/* 2046 */
	{4, 6, 5, 5, 6, 7, 7, 8, 8, 7, 7, 6},	/* 2047 */
	{4, 5, 4, 5, 5, 6, 7, 7, 7, 7, 6, 5},	/* 2048 */
	{3, 5, 4, 5, 5, 6, 7, 7, 8, 7, 7, 5}	/* 2049 */
	};
	
	//=========================================================
	private static int []daysInSolarMonth = {
		0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	private static int []moon = {29,30}; /* a short (long) lunar month has 29 (30) days */

	private final static	String GanGB[] = {
	    "甲", "乙", "丙", "丁", "戊",
	    "己", "庚", "辛", "壬", "癸"
	};

	private final static	String ZhiGB[] = {
	    "子", "丑", "寅", "卯", "辰", "巳",
	    "午", "未", "申", "酉", "戌", "亥"
	};

	private final static	String ShengXiaoGB[] = {
	    "鼠", "牛", "虎", "兔", "龙", "蛇",
	    "马", "羊", "猴", "鸡", "狗", "猪"
	};

	private final static String weekdayGB[] = {
	    "日", "一", "二", "三",
	    "四", "五", "六"
	};
	
	
	private final static String solarTerm[] = {
		"小寒","大寒","立春","雨水",
		"惊蛰","春分","清明","谷雨",
		"立夏","小满","芒种","夏至",
		"小暑","大暑","立秋","处暑",
		"白露","秋分","寒露","霜降",
		"立冬","小雪","大雪","冬至"
	};
	
	private Date solar = new Date(); 
	private Date lunar = new Date(); 
	private Date gan = new Date(); 
	private Date zhi = new Date(); 
	
	
	private Date gan2 = new Date(); 
	private Date zhi2 = new Date(); 
	private Date lunar2 = new Date(); 
	
	private int ymonth[] = new int[Nyear];//一年中阴历月数
	private int yday[] = new int[Nyear];
	private int mday[] = new int[Nmonth+1];
	
	private final static int BYEAR = 1201;
	
	public boolean LeapYear(int y)
	{
		return (((y)%4==0) && ((y)%100!=0) || ((y)%400==0));
	}
	
	private void CalGZ(long offset,Date d,Date g, Date z)
	{
		int year, month;
		year = d.getYear() - LunarFirstDate.getYear();
		month = year*12 + d.getMonth() - 1;
		
		g.setYear((GanFirstDate.getYear() + year)%10);
		z.setYear((ZhiFirstDate.getYear() + year)%12);
		
		g.setMonth((GanFirstDate.getMonth() + month)%10);
		z.setMonth((ZhiFirstDate.getMonth() + month)%12);
		
		g.setDay((int)(GanFirstDate.getDay() + offset)%10);
		z.setDay((int)(ZhiFirstDate.getDay() + offset)%12);
		
		z.setHour(((d.getHour() + 1)/2)%12);
		g.setHour((g.getDay()*12 + z.getHour())%10);
	}
	
	private int make_yday()
	{
		int year, i, leap;
	    int code;
	    
	    for (year = 0; year < Nyear; year++)
	    {
		code = (int)yearInfo[year];
		leap = code & 0xf;
		yday[year] = 0;
		if (leap != 0)
		{
		    i = (code >> 16) & 0x1;
		    yday[year] += moon[i];
		}
		code >>= 4;
		for (i = 0; i < Nmonth-1; i++)
		{
		    yday[year] += moon[code & 0x1];
		    code >>= 1;
		}
		ymonth[year] = 12;
		if (leap != 0) ymonth[year]++;
	    }
	    return Nyear;
	}
	
	private int make_mday(int year)
	{
		int i, leapMonth;
	    int code;
	    
	    code = (int)yearInfo[year];
	    leapMonth = code & 0xf;
	    /* leapMonth == 0 means no leap month */
	    code >>= 4;
	    if (leapMonth == 0)
	    {
		mday[Nmonth] = 0;
		for (i = Nmonth-1; i >= 1; i--)
		{
		    mday[i] = moon[code & 0x1];
		    code >>= 1;
		}
	    }
	    else
	    {
		/* 
		  There is a leap month (run4 yue4) L in this year.
		  mday[1] contains the number of days in the 1-st month;
		  mday[L] contains the number of days in the L-th month;
		  mday[L+1] contains the number of days in the L-th leap month;
		  mday[L+2] contains the number of days in the L+1 month, etc.

		  cf. yearInfo[]: info about the leap month is encoded differently.
		*/
		i = ((int)(yearInfo[year] >> 16)) & 0x1;
		mday[leapMonth+1] = moon[i];
		for (i = Nmonth; i >= 1; i--)
		{
		    if (i == leapMonth+1) i--;
		    mday[i] = moon[code&0x1];
		    code >>= 1;
		}
	    }
	    return leapMonth;
	}
	
	
	private long Solar2Day1(Date d)
	{
		long offset, delta;
		int i;
		
		delta = d.getYear() - BYEAR;
		if(delta < 0)
		{
			System.out.println("内部错误！需要重新设置更大的BYEAR");
		}
		
		offset = delta *365 + delta / 4 - delta / 100 + delta / 400;
		for(i=1; i<d.getMonth(); i++)
		{
			offset += daysInSolarMonth[i];
		}
		
		if((d.getMonth()>2)&& LeapYear(d.getYear()))
		{
			offset++;
		}
		
		offset+=d.getDay() - 1;
		
		if((d.getMonth() == 2)&& LeapYear(d.getYear()))
		{
			if(d.getDay()>29)
			{
				System.out.println("Day越界");
			}
		}
		else if(d.getDay() > daysInSolarMonth[d.getMonth()])
		{
			System.out.println("Day越界");
		}
		
		return offset;
	}
	
	
	private long Lunar2Day(Date d)
	{
		long offset = 0;
		int year, i, m, nYear, leapMonth;
		
		nYear = make_yday();
		year = d.getYear() - LunarFirstDate.getYear();
		
		for(i=0; i<year; i++)
		{
			offset += yday[i];
		}
		
		leapMonth = make_mday(year);
		
		if((d.getLeap() != 0) && (leapMonth != d.getMonth()))
		{
			System.out.println(d.getMonth() + "is not a leap month inyear" + d.getYear());
			return -1;
		}
		
		for(m=1; m<d.getMonth();m++)
		{
			offset+=mday[m];
		}
		
		if((leapMonth != 0) && ((d.getMonth() > leapMonth) || ((d.getLeap()!=0) && (d.getMonth() == leapMonth))))
		{
			offset+=mday[m++];
		}
		
		offset+=d.getDay() -1;
		
		if(d.getDay() > mday[m])
		{
			System.out.println("Day越界");
		}
		
		return offset;
	}
	
	
	private void Day2Lunar(long offset, Date d)
	{
		int i, m, nYear, leapMonth;
		
		nYear = make_yday();
		for(i=0; i<nYear && offset>0; i++)
		{
			offset -= yday[i];
		}
		
		if(offset<0)//fox
		{
			offset+=yday[--i];
		}
		
		if(i==Nyear)
		{
			System.out.println("Year越界");
		}
		
		d.setYear(i+LunarFirstDate.getYear());
		
		leapMonth = make_mday(i);
		
		for(m=1;(m<Nmonth) && (offset>0);m++)
		{
			offset -= mday[m];
		}
		
		if(offset<0)
		{
			offset += mday[--m];
		}
		
		d.setLeap(0);
		
		if(leapMonth>0)
		{
			if(leapMonth == (m-1))
			{
				d.setLeap(1);	
			}
			else
			{
				d.setLeap(0);
			}
			
			if(m>leapMonth)
			{
				--m;
			}
		}
		
		d.setMonth(m);
		d.setDay((int)(offset+1));	
	}
	
	private long Solar2Day(Date d)
	{
		return (Solar2Day1(d) - Solar2Day1(SolarFirstDate));
	}
	
	
	
	private void Day2Solar(long offset, Date d)
	{
		int i, m, days;
		
		offset -= Solar2Day(LunarFirstDate);
		
		for(i=SolarFirstDate.getYear(); (i<(SolarFirstDate.getYear() + Nyear)) && (offset > 0) ; ++i)
		{
			if(LeapYear(i))//logfox
			{
				offset -= 366;
			}
			else
			{
				offset -= 365;
			}
			
		}
		
		if(offset<0)
		{
			--i;
			if(LeapYear(i))//logfox
			{
				offset +=366;
			}
			else
			{
				offset += 365;
			}
		}
		
		if(i==(SolarFirstDate.getYear() + Nyear))
		{
			System.out.println("Year越界");
		}
		
		d.setYear(i);
		
		for(m=1; m<=12; m++)
		{
			days = daysInSolarMonth[m];
			if((m==2)&&(LeapYear(i)))
			{
				days++;
			}
			
			if(offset<days)
			{
				d.setMonth(m);
				d.setDay((int)(offset+1));
				return;
			}
			offset -= days;
		}
	}
	
	private int GZcycle(int g, int z)
	{
		int gz;
		
		for(gz = z; gz %10 != g && gz < 60; gz +=12);
		if(gz >=60){System.out.println("内部错误");}
		
		return gz+1;
	}
	
	public int CmpDate(int month1, int day1, int month2, int day2)
	{
		if(month1 != month2)return (month1 - month2);
		if(day1 != day2)return (day1 - day2);
		return 0;
	}
	
	
	private int JieDate(Date ds, Date dl)
	{
		int m, flag=0;
		
		if(ds.getMonth() == 1)
		{
			flag = CmpDate(ds.getMonth(),ds.getDay(),1,fest[ds.getYear() - SolarFirstDate.getYear() - 1][11]);
			
			if(flag<0)dl.setMonth(11);
			else if(flag>0)dl.setMonth(12);
			
			dl.setYear(ds.getYear() -1);
			
			return flag==0 ? 1 : 0;
		}
		
		for(m=2; m<=12; m++)
		{
			flag = CmpDate(ds.getMonth(),ds.getDay(),m,fest[ds.getYear() - SolarFirstDate.getYear()][m-2]);
			if(flag == 0)m++;
			if(flag<=0)break;
		}
		
		dl.setMonth((m-2)%12);
		dl.setYear(ds.getYear());
		
		if((dl.getMonth())==0)
		{
			dl.setYear(ds.getYear()-1);
			dl.setMonth(12);
		}
		
		return flag == 0 ? 1 : 0;
	}
	
	public void Solar2Lunar()
	{
		long offset;
		Date d;
		
		offset = Solar2Day(solar);
		solar.setWeekDay((int)(offset + SolarFirstDate.getWeekDay())%7);
		
		if(solar.getHour() == 23)offset++;
		
		Day2Lunar(offset,lunar);
		lunar.setHour(solar.getHour());
		CalGZ(offset,lunar,gan,zhi);
		jieAlert = JieDate(solar,lunar2);
		lunar2.setDay(lunar.getDay());
		lunar2.setHour(lunar.getHour());
		CalGZ(offset,lunar2,gan2,zhi2);
		
	}
	
	public void Lunar2Solar()
	{
		long offset;
		int adj;
		Date d;
		
		adj = lunar.getHour() == 23 ? -1 : 0;
		offset = Lunar2Day(lunar);
		solar.setWeekDay((int)(offset + adj + SolarFirstDate.getWeekDay())%7);
		Day2Solar(offset+adj,solar);
		solar.setHour(lunar.getHour());
		CalGZ(offset,lunar,gan,zhi);
		
		jieAlert = JieDate(solar,lunar2);
		lunar2.setDay(lunar.getDay());
		lunar2.setHour(lunar.getHour());
		CalGZ(offset,lunar2,gan2,zhi2);
	}
	
	
	//************************************************************************************
	//  函数功能： 二十四节气数据库
	//  入口参数： unsigned char(yy,mo,dd) 对应 年月日
	//  出口参数： unsigned char(0-24) 1-24对应二十四节气
	//							二十四节气数据库（1901--2050） 
	//	数据格式说明:   
	//	如1901年的节气为   
	//     1月     2月         3月           4月           5月           6月           7月         8月         9月             10月     11月       12月   
	// [ 6,21][ 4,19][ 6,21][ 5,21][ 6,22][ 6,22][ 8,23][ 8,24][ 8,24][ 8,24][ 8,23][ 8,22]   
	// [ 9, 6][11, 4][ 9, 6][10, 6][ 9, 7][ 9, 7][ 7, 8][ 7, 9][ 7, 9][ 7, 9][ 7, 8][ 7,15]   
	//		上面第一行数据为每月节气对应公历日期,15减去每月第一个节气,每月第二个节气减去15得第二
	//		行，这样每月两个节气对应数据都小于16,每月用一个字节存放,高位存放第一个节气数据,低位存
	//		放第二个节气的数据,可得下表   
	//**************************************************************************************
	private final static int jieqiCode[] = {
		   0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1901   
	       0x96,0xA4,0x96,0x96,0x97,0x87,0x79,0x79,0x79,0x69,0x78,0x78,     //1902   
	       0x96,0xA5,0x87,0x96,0x87,0x87,0x79,0x69,0x69,0x69,0x78,0x78,     //1903   
	       0x86,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x78,0x87,     //1904   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1905   
	       0x96,0xA4,0x96,0x96,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1906   
	       0x96,0xA5,0x87,0x96,0x87,0x87,0x79,0x69,0x69,0x69,0x78,0x78,     //1907   
	       0x86,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1908   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1909   
	       0x96,0xA4,0x96,0x96,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1910   
	       0x96,0xA5,0x87,0x96,0x87,0x87,0x79,0x69,0x69,0x69,0x78,0x78,     //1911   
	       0x86,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1912   
	       0x95,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1913   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1914   
	       0x96,0xA5,0x97,0x96,0x97,0x87,0x79,0x79,0x69,0x69,0x78,0x78,     //1915   
	       0x96,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1916   
	       0x95,0xB4,0x96,0xA6,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x87,     //1917   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x77,     //1918   
	       0x96,0xA5,0x97,0x96,0x97,0x87,0x79,0x79,0x69,0x69,0x78,0x78,     //1919   
	       0x96,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1920   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x87,     //1921   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x77,     //1922   
	       0x96,0xA4,0x96,0x96,0x97,0x87,0x79,0x79,0x69,0x69,0x78,0x78,     //1923   
	       0x96,0xA5,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1924   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x87,     //1925   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1926   
	       0x96,0xA4,0x96,0x96,0x97,0x87,0x79,0x79,0x79,0x69,0x78,0x78,     //1927   
	       0x96,0xA5,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1928   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1929   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1930   
	       0x96,0xA4,0x96,0x96,0x97,0x87,0x79,0x79,0x79,0x69,0x78,0x78,     //1931   
	       0x96,0xA5,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1932   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1933   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1934   
	       0x96,0xA4,0x96,0x96,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1935   
	       0x96,0xA5,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1936   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1937   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1938   
	       0x96,0xA4,0x96,0x96,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1939   
	       0x96,0xA5,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1940   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1941   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1942   
	       0x96,0xA4,0x96,0x96,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1943   
	       0x96,0xA5,0x96,0xA5,0xA6,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1944   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1945   
	       0x95,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x78,0x69,0x78,0x77,     //1946   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1947   
	       0x96,0xA5,0xA6,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //1948   
	       0xA5,0xB4,0x96,0xA5,0x96,0x97,0x88,0x79,0x78,0x79,0x77,0x87,     //1949   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x77,     //1950   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x79,0x79,0x79,0x69,0x78,0x78,     //1951   
	       0x96,0xA5,0xA6,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //1952   
	       0xA5,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1953   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x78,0x79,0x78,0x68,0x78,0x87,     //1954   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1955   
	       0x96,0xA5,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //1956   
	       0xA5,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1957   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1958   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1959   
	       0x96,0xA4,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x88,0x78,0x87,0x87,     //1960   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1961   
	       0x96,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1962   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1963   
	       0x96,0xA4,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x88,0x78,0x87,0x87,     //1964   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1965   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1966   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1967   
	       0x96,0xA4,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //1968   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1969   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1970   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x79,0x69,0x78,0x77,     //1971   
	       0x96,0xA4,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //1972   
	       0xA5,0xB5,0x96,0xA5,0xA6,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1973   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1974   
	       0x96,0xB4,0x96,0xA6,0x97,0x97,0x78,0x79,0x78,0x69,0x78,0x77,     //1975   
	       0x96,0xA4,0xA5,0xB5,0xA6,0xA6,0x88,0x89,0x88,0x78,0x87,0x87,     //1976   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //1977   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x78,0x87,     //1978   
	       0x96,0xB4,0x96,0xA6,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x77,     //1979   
	       0x96,0xA4,0xA5,0xB5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //1980   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x77,0x87,     //1981   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1982   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x78,0x79,0x78,0x69,0x78,0x77,     //1983   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x87,     //1984   
	       0xA5,0xB4,0xA6,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //1985   
	       0xA5,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //1986   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x79,0x78,0x69,0x78,0x87,     //1987   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //1988   
	       0xA5,0xB4,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x88,0x78,0x87,0x87,     //1989   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x79,0x77,0x87,     //1990   
	       0x95,0xB4,0x96,0xA5,0x86,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1991   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //1992   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x88,0x78,0x87,0x87,     //1993   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1994   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x76,0x78,0x69,0x78,0x87,     //1995   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //1996   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //1997   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //1998   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //1999 
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2000   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2001   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //2002   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //2003   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2004   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2005   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2006   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x69,0x78,0x87,     //2007   
	       0x96,0xB4,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x87,0x78,0x87,0x86,     //2008   
	       0xA5,0xB3,0xA5,0xB5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2009   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2010   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x78,0x87,     //2011   
	       0x96,0xB4,0xA5,0xB5,0xA5,0xA6,0x87,0x88,0x87,0x78,0x87,0x86,     //2012   
	       0xA5,0xB3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x87,     //2013   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2014   
	       0x95,0xB4,0x96,0xA5,0x96,0x97,0x88,0x78,0x78,0x79,0x77,0x87,     //2015   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x87,0x88,0x87,0x78,0x87,0x86,     //2016   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x87,     //2017   
	       0xA5,0xB4,0xA6,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2018   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x79,0x77,0x87,     //2019   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x86,     //2020   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2021   
	       0xA5,0xB4,0xA5,0xA5,0xA6,0x96,0x88,0x88,0x88,0x78,0x87,0x87,     //2022   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x79,0x77,0x87,     //2023   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x96,     //2024   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2025   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2026   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //2027   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x96,     //2028   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2029   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2030   
	       0xA5,0xB4,0x96,0xA5,0x96,0x96,0x88,0x78,0x78,0x78,0x87,0x87,     //2031   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x96,     //2032   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x86,     //2033   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x78,0x88,0x78,0x87,0x87,     //2034   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2035   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x96,     //2036   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x86,     //2037   
	       0xA5,0xB3,0xA5,0xA5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2038   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2039   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x96,     //2040   
	       0xA5,0xC3,0xA5,0xB5,0xA5,0xA6,0x87,0x88,0x87,0x78,0x87,0x86,     //2041   
	       0xA5,0xB3,0xA5,0xB5,0xA6,0xA6,0x88,0x88,0x88,0x78,0x87,0x87,     //2042   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2043   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA6,0x97,0x87,0x87,0x88,0x87,0x96,     //2044   
	       0xA5,0xC3,0xA5,0xB4,0xA5,0xA6,0x87,0x88,0x87,0x78,0x87,0x86,     //2045   
	       0xA5,0xB3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x88,0x78,0x87,0x87,     //2046   
	       0xA5,0xB4,0x96,0xA5,0xA6,0x96,0x88,0x88,0x78,0x78,0x87,0x87,     //2047   
	       0x95,0xB4,0xA5,0xB4,0xA5,0xA5,0x97,0x87,0x87,0x88,0x86,0x96,     //2048   
	       0xA4,0xC3,0xA5,0xA5,0xA5,0xA6,0x97,0x87,0x87,0x78,0x87,0x86,     //2049   
	       0xA5,0xC3,0xA5,0xB5,0xA6,0xA6,0x87,0x88,0x78,0x78,0x87,0x87      //2050	
	};
	
	//**************************************************************
	// 获取日期属性信息的方法
	//**************************************************************
	public String getJieQi()
	{
		String noInfo = "noInfo";
		int year,month,day,temp;
		year = solar.getYear() - 1901;
		month = solar.getMonth();
		day = solar.getDay();
		
		int offset = year*12 + month -1;
		if(day<15)
		{
			temp = 15 - day;
			if((jieqiCode[offset]>>4) == temp)
			{
				return solarTerm[(month<<1)-2];
			}
			else return noInfo;
		}
		
		if(day == 15)
		{
			return noInfo;
		}
		
		if(day > 15)
		{
			temp = day - 15;
			if((jieqiCode[offset] & 0x0f) == temp)
			{
				return solarTerm[((month<<1)-1)];
			}
			else
			{
				return noInfo;
			}
		}
		
		
		return noInfo;
	}
	
	//**************************************************************
	// 构造函数
	//**************************************************************
	private int INDEX;//每个月首日的位置
	private int END;//每个月的天数
	private int ROWS;
	public Calendar()
	{
		solar.setDate(Birthday);
		this.setCalendarArgs();
	}
	
	private void setCalendarArgs()
	{
		Date origDate = new Date(solar);
		solar.setDate(new Date(solar.getYear(),solar.getMonth(),1));
        INDEX = getSolarDate().getWeekDay();
        END = getDaysInSolarMonth();
        solar.setDate(origDate);
        
        if((END+INDEX)%7==0)
        {
        	ROWS = (END+INDEX)/7 + 1;
        }
        else
        {
        	ROWS = (END+INDEX)/7 + 2;
        }
	}
	
	
	public int getCalendarIndex()
	{
		return INDEX;
	}
	
	public int getCalendarEnd()
	{
		return END;
	}
	
	public int getCalendarRows()
	{
		return ROWS;
	}
	
	
	
	//**************************************************************
	// set与get方法
	//**************************************************************
	
	public void setSolarDate(Date solar)
	{
		this.solar.setYear(solar.getYear());
		this.solar.setMonth(solar.getMonth());
		this.solar.setDay(solar.getDay());
		this.solar.setHour(solar.getHour());
		this.solar.setWeekDay(solar.getWeekDay());
		this.solar.setLeap(solar.getLeap());
		this.setCalendarArgs();
	}
	
	public Date getSolarDate()
	{
		this.getSolarWeekDay();
		return this.solar;
	}
	
	public void setLunarDate(Date lunar)
	{
		this.lunar.setYear(lunar.getYear());
		this.lunar.setMonth(lunar.getMonth());
		this.lunar.setDay(lunar.getDay());
		this.lunar.setHour(lunar.getHour());
		this.lunar.setWeekDay(lunar.getWeekDay());
		this.lunar.setLeap(lunar.getLeap());
	}
	
	public Date getLunarDate()
	{
		return this.lunar;
	}
	
	public String getSolarWeekDay()
	{
		long offset;
		offset = Solar2Day(solar);
		solar.setWeekDay((int)(offset + SolarFirstDate.getWeekDay())%7);
		return weekdayGB[solar.getWeekDay()];
	}
	
	public String getShengXiao()
	{
		return ShengXiaoGB[zhi.getYear()];
	}
	
	public String getGanZhiYear()
	{
		return GanGB[gan.getYear()]+ZhiGB[zhi.getYear()];
	}
	
	public String getGanZhiMonth()
	{
		return GanGB[gan.getMonth()]+ZhiGB[zhi.getMonth()];
	}
	
	public String getGanZhiDay()
	{
		return GanGB[gan.getDay()]+ZhiGB[zhi.getDay()];
	}
	
	public int getDaysInSolarMonth()
	{
		if(solar.getMonth() == 2 && LeapYear(solar.getYear()))
		{
			return daysInSolarMonth[solar.getMonth()] + 1;
		}
		
		return daysInSolarMonth[solar.getMonth()];
	}
}

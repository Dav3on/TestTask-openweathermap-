package com.openweathermap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Cities {

    /*List of city ID city.list.json.gz can be downloaded here http://bulk.openweathermap.org/sample/
    The service isn't provide the list of postal codes so i get it from http://www.geopostcodes.com/Ukraine*/
    public static final ArrayList<HashMap> CITIES = new ArrayList<HashMap>(Arrays.asList(
            new HashMap() {{
                put("name", "Kiev");
                put("id", 703448);
                put("country", "UA");
                put("lon", 30.516666);
                put("lat", 50.433334);
                put("zip", 80363);
            }},
            new HashMap() {{
                put("name", "Ternopil");
                put("id", 691650);
                put("country", "UA");
                put("lon", 25.60556);
                put("lat", 49.555889);
                put("zip", 61101);
            }},
            new HashMap() {{
                put("name", "Kherson");
                put("id", 706448);
                put("country", "UA");
                put("lon", 32.617802);
                put("lat", 46.655811);
                put("zip", 65101);
            }},
            new HashMap() {{
                put("name", "Lutsk");
                put("id", 702569);
                put("country", "UA");
                put("lon", 25.34244);
                put("lat", 50.759319);
                put("zip", 43001);
            }},
            new HashMap() {{
                put("name", "Irpin");
                put("id", 707565);
                put("country", "UA");
                put("lon", 30.250549);
                put("lat", 50.521751);
                put("zip", 8201);
            }}
    ));
}

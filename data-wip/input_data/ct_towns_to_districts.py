"""
README

The connecticut dataset doesn't have congressional districts per precinct
However it does give town data (in convenient all-caps form)

Town->district data grabbed from:
http://www.ctn.state.ct.us/civics/maps/Connecticut%20Congressional%20Districts_text.htm


NOTE: MIDDLETOWN, TORRINGTON, WATERBURY, DURHAM, SHELTON, GLASTONBURY were all listed in two districts at once
I've moved them all to the lower of the two districts theyre in

"""

import pandas as pd


_districts_to_towns = {

"CT01": [
"Barkhamsted", "Berlin", "Bloomfield", "Bristol", "Colebrook", "Cromwell", "East Granby", "East Hartford", "East Windsor", "Glastonbury",
"Granby", "Hartford", "Hartland", "Manchester", "Middletown", "New Hartford", "Newington", "Portland", "Rocky Hill",
"Southington", "South Windsor", "Torrington", "West Hartford", "Wethersfield", "Winchester", "Windsor", "Windsor Locks",
],
			

"CT02": [
"Andover", "Ashford", "Bolton", "Bozrah", "Brooklyn", "Canterbury", "Chaplin", "Chester", "Clinton", "Colchester",
"Columbia", "Coventry", "Deep River", "Durham", "East Haddam", "East Hampton", "East Lyme", "Eastford", "Ellington", "Enfield",
"Essex", "Franklin", "Griswold", "Groton", "Haddam", "Hampton", "Hebron", "Killingly", "Killingworth",
"Lebanon", "Ledyard", "Lisbon", "Lyme", "Madison", "Mansfield", "Marlborough", "Montville", "New London", "North Stonington",
"Norwich", "Old Lyme", "Old Saybrook", "Plainfield", "Pomfret", "Preston", "Putnam", "Salem", "Scotland",
"Somers", "Sprague", "Stafford", "Sterling", "Stonington", "Suffield", "Thompson", "Tolland",
"Union", "Vernon", "Voluntown", "Waterford", "Westbrook", "Willington", "Windham", "Woodstock",
],
			

"CT03": [
"Ansonia", "Beacon Falls", "Bethany", "Branford", "Derby", "East Haven", "Guilford", "Hamden", "Middlefield", 
"Milford", "Naugatuck", "New Haven", "North Branford", "North Haven", "Orange", "Prospect", "Seymour",
"Shelton", "Stratford", "Wallingford", "Waterbury", "West Haven", "Woodbridge",
],
			
"CT04": [
"Bridgeport", "Darien", "Easton", "Fairfield", "Greenwich", "Monroe", "New Canaan", "Norwalk", "Oxford", "Redding",
"Ridgefield", "Stamford", "Trumbull", "Weston", "Westport", "Wilton",
],
			

"CT05": [
"Avon", "Bethel", "Bethlehem", "Bridgewater", "Brookfield", "Burlington", "Canaan", "Canton", "Cheshire",
"Cornwall", "Danbury", "Farmington", "Goshen", "Harwinton", "Kent", "Litchfield", "Meriden",
"Middlebury", "Morris", "New Britain", "New Fairfield", "New Milford", "Newtown", "Norfolk", "North Canaan", "Plainville", "Plymouth", "Roxbury",
"Salisbury", "Sharon", "Sherman", "Simsbury", "Southbury", "Thomaston",
"Warren", "Washington", "Watertown", "Wolcott", "Woodbury",
],
}


towns_to_districts = { town.upper(): district for (district, towns) in _districts_to_towns.items() 
                                                 for town in towns}

#towns_and_districts = pd.DataFrame(_towns_to_districts, columns=["TOWN", "DISTRICT"])

<?php
	include "dbConnect.php";

	$name   = $_POST['name'];
	
	class emp{}
	
	$query = mysql_query("SELECT i.found_id, i.broken_id, i.found_date, i.found_note, ii.broken_date, iii.sub_stuff_serial_number, iiii.stuff_name
								FROM invent_found i
								INNER JOIN invent_broken ii ON i.broken_id = ii.broken_id
								INNER JOIN invent_sub_stuff iii ON ii.sub_stuff_id = iii.sub_stuff_id
								INNER JOIN invent_stuff iiii ON iii.stuff_id = iiii.stuff_id
								WHERE ii.broken_status='4' AND iiii.stuff_name='".$name."' " );
	
	$json = array();
	
	while($row = mysql_fetch_assoc($query)){
		$json[] = $row;
	}
	
	echo json_encode($json);
	
	mysql_close($connect);
?>
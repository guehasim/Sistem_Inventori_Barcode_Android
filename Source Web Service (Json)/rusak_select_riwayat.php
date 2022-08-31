<?php
	include "dbConnect.php";
	
	$query = mysql_query("SELECT i.repair_id, i.repair_start_date, i.broken_id, i.repair_repairer,
							ii.sub_stuff_id, ii.broken_problem, ii.broken_date, iii.sub_stuff_serial_number,
							iii.stuff_id, iiii.stuff_name 
								FROM invent_repair i
								INNER JOIN invent_broken ii ON i.broken_id = ii.broken_id
								INNER JOIN invent_sub_stuff iii ON ii.sub_stuff_id = iii.sub_stuff_id
								INNER JOIN invent_stuff iiii ON iii.stuff_id = iiii.stuff_id ");
	
	$json = array();
	
	while($row = mysql_fetch_assoc($query)){
		$json[] = $row;
	}
	
	echo json_encode($json);
	
	mysql_close($connect);
?>
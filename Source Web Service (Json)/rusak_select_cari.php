<?php
	include "dbConnect.php";

	$name   = $_POST['name'];
	
	class emp{}
	
	$query = mysql_query("SELECT i.broken_id, i.broken_problem, i.broken_date, ii.sub_stuff_id, ii.sub_stuff_serial_number, iii.stuff_name
								FROM invent_broken i
								INNER JOIN invent_sub_stuff ii ON i.sub_stuff_id = ii.sub_stuff_id
								INNER JOIN invent_stuff iii ON ii.stuff_id = iii.stuff_id
								WHERE broken_status = '0' AND iii.stuff_name='".$name."' ");
	
	$json = array();
	
	while($row = mysql_fetch_assoc($query)){
		$json[] = $row;
	}
	
	echo json_encode($json);
	
	mysql_close($connect);
?>
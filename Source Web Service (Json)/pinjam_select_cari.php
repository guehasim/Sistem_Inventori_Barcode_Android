<?php
	include "dbConnect.php";

	$name   = $_POST['name'];
	
	class emp{}
	
	$query = mysql_query("SELECT i.borrow_id, i.borrow_date, i.borrow_borrower, i.borrow_note, ii.sub_stuff_id, iii.sub_stuff_serial_number, iiii.stuff_name
								FROM invent_borrow i
								INNER JOIN invent_borrow_detail ii ON i.borrow_id = ii.borrow_id
								INNER JOIN invent_sub_stuff iii ON ii.sub_stuff_id = iii.sub_stuff_id
								INNER JOIN invent_stuff iiii ON iii.stuff_id = iiii.stuff_id 
								WHERE borrow_return_date IS NULL AND iiii.stuff_name='".$name."' ");
	
	$json = array();
	
	while($row = mysql_fetch_assoc($query)){
		$json[] = $row;
	}
	
	echo json_encode($json);
	
	mysql_close($connect);
?>
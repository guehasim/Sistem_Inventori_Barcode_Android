<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];
	$name   = $_POST['name'];
	
	class emp{}
	
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Data Tidak Boleh kosong"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT i.stuff_id, i.stuff_name, i.stuff_brand, i.stuff_model, ii.sub_stuff_id, ii.parent_id, ii.sub_stuff_serial_number, ii.sub_stuff_condition, ii.sub_stuff_borrow, ii.sub_stuff_year_purchase
								FROM invent_stuff i
								INNER JOIN invent_sub_stuff ii ON i.stuff_id = ii.stuff_id
								WHERE ii.parent_id='".$id."' AND i.stuff_name='".$name."' ");

		$json = array();

		while ($row = mysql_fetch_assoc($query)) {
			$json[] = $row;
		}
		echo json_encode($json);
		mysql_close($connect);

	}
?>
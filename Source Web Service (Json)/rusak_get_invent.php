<?php
	include "dbConnect.php";
	
	$id 	= $_POST['serial'];
	
	class emp{}
	 
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data id"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT i.stuff_name, ii.sub_stuff_id, ii.sub_stuff_serial_number, ii.sub_stuff_condition, ii.sub_stuff_borrow
								FROM invent_stuff i
								INNER JOIN invent_sub_stuff ii ON i.stuff_id = ii.stuff_id
								WHERE ii.sub_stuff_serial_number ='".$id."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->stuff_name				= $row["stuff_name"];
			$response->sub_stuff_id 			= $row["sub_stuff_id"];
			$response->sub_stuff_serial_number	= $row["sub_stuff_serial_number"];
			$response->sub_stuff_condition		= $row['sub_stuff_condition'];
			$response->sub_stuff_borrow			= $row['sub_stuff_borrow'];
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>
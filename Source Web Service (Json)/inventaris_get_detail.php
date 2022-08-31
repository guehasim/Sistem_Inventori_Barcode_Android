<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];
	
	class emp{}
	 
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data id"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT 	i.stuff_id, i.stuff_name, i.stuff_brand, i.stuff_model, ii.parent_id, ii.sub_stuff_id, ii.sub_stuff_serial_number, ii.sub_stuff_condition, ii.sub_stuff_borrow, ii.sub_stuff_year_purchase
								FROM invent_stuff i
								INNER JOIN invent_sub_stuff ii ON i.stuff_id = ii.stuff_id
								WHERE ii.sub_stuff_id ='".$id."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->stuff_id					= $row["stuff_id"];
			$response->stuff_name				= $row["stuff_name"];
			$response->stuff_brand				= $row["stuff_brand"];
			$response->stuff_model				= $row["stuff_model"];
			$response->sub_stuff_id				= $row["sub_stuff_id"];
			$response->parent_id				= $row["parent_id"];
			$response->sub_stuff_serial_number	= $row["sub_stuff_serial_number"];
			$response->sub_stuff_condition		= $row["sub_stuff_condition"];
			$response->sub_stuff_borrow			= $row["sub_stuff_borrow"];
			$response->sub_stuff_year_purchase	= $row["sub_stuff_year_purchase"];
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>
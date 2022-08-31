<?php
	include "dbConnect.php";
	
	$serial 	= $_POST['serial'];
	
	class emp{}
	 
	if (empty($serial)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data id"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT * FROM invent_sub_stuff WHERE sub_stuff_serial_number ='".$serial."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->sub_stuff_id 			= $row["sub_stuff_id"];
			$response->parent_id				= $row["parent_id"];
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>
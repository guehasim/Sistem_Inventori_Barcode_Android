<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];
	
	class emp{}
	 
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data serial"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT * FROM invent_sub_stuff WHERE sub_stuff_serial_number='".$id."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->sub_stuff_id 			= $row["sub_stuff_id"];
			$response->sub_stuff_borrow			= $row["sub_stuff_borrow"];
			
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>
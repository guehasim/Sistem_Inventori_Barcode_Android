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
		$query 	= mysql_query("SELECT i.broken_id, ii.sub_stuff_id 
								FROM invent_repair i
								INNER JOIN invent_broken ii ON i.broken_id = ii.broken_id
								WHERE i.repair_id='".$id."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->broken_id				= $row["broken_id"];
			$response->sub_stuff_id				= $row['sub_stuff_id'];
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>
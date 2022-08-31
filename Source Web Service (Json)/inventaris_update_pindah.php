<?php
	include "dbConnect.php";
	
	$id 		= $_POST['sub_stuff_id'];
	$parent		= $_POST['parent_id'];

	class emp{}
	
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {
		$query = mysql_query("UPDATE invent_sub_stuff SET parent_id='".$parent."' WHERE sub_stuff_id='".$id."' ");
		
		if ($query) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di update";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error update Data";
			die(json_encode($response)); 
		}	
	}
?>
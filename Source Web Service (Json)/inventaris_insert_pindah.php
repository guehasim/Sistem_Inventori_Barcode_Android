<?php
	include "dbConnect.php";
	
	$sub_stuff_id	= $_POST['sub_stuff_id'];
	$id_before		= $_POST['id_before'];
	$date			= $_POST['date']; 
//Creating an sql query 
	
	class emp{}
	
	if (empty($sub_stuff_id) || empty($id_before)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_move_stuff(move_id, sub_stuff_id, parent_id_before, move_date) 
			VALUES (0,'".$sub_stuff_id."','".$id_before."','".$date."')");

		if ($query) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di simpan";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error simpan Data";
			die(json_encode($response)); 
		}	
	}
?>
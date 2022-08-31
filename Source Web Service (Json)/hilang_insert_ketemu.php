<?php
	include "dbConnect.php";
	
	$broken_id	= $_POST['broken_id'];
	$date 		= $_POST['ketemu_date'];
	$note 		= $_POST['ketemu_note'];
//Creating an sql query 
	
	class emp{}
	
	if (empty($broken_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_found(found_id,broken_id,found_date,found_note) 
			VALUES (0,'".$broken_id."','".$date."','".$note."')");
		
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


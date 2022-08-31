<?php
	include "dbConnect.php";
	
	$usr 	= $_POST['user'];
	$pas		= $_POST['pass'];
	
	class emp{}
	 
	if (empty($usr )) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data id"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT * FROM login WHERE login_user ='".$usr."' AND login_pass ='".$pas."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Data yang anda masukan salah";
			die(json_encode($response)); 
		}	
	}
?>
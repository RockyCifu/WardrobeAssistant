<?php
	require_once'../includes/db_operations.php';
	$response = array();
	
	if($_SERVER['REQUEST_METHOD']=='POST')
	{

		$db = new DbOperations();
		//read the input parameters
		$image_path =$_POST['image_path'];
		$user_id = $_POST['user_id'];
		
		//update last viewed date for the item
		$result = $db->updateDate($image_path, $user_id);
		if($result == true)
		{
			$response['error']=false;
			$response['message']='Date update was succesful'; 
		}
		else
		{
			$response['error']=true;
			$response['message']='Something went wrong'; 
		}
	} 
	else
	{
		$response['error'] = true;
		$response['message']= 'Invalid Request';
	}
	echo json_encode($response);
?>
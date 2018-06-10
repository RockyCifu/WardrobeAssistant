<?php
	require_once'../includes/db_operations.php';
	$response = array();
	
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		if(isset($_POST['user_name'])and
			isset($_POST['password'])
			)
		{
			$db = new DbOperations();
			//login the user
			if($db->userLogin($_POST['user_name'], $_POST['password']))
			{
				//read the user name
				$user = $db->getUserByUsername($_POST['user_name']);
				$response['error'] = false;
				$response['user_id']= $user['user_id'];
				$response['user_name']= $user['user_name'];
			}
			else
			{
				$response['error'] = true;
				$response['message']= 'Invalid username or password';
				
			}
		}
		else
		{
			$response['error'] = true;
			$response['message']= 'Required fields are missing';
		}
	}
	echo json_encode($response);
?>
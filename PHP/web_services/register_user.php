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
			
			//create a user account
			$result = $db->createUser(
				$_POST['user_name'],
				$_POST['password']);
			if($result == 1)
			{
				$response['error'] = false;
				$response['message']= 'User registered successfully';
			}
			elseif($result == 2)
			{
				$response['error'] = true;
				$response['message']= 'Some error occured please try again';
			}
			elseif($result == 0)
			{
				$response['error'] = true;
				$response['message']= 'User already exists';
			}
			elseif($result == 3)
			{
				$response['error'] = true;
				$response['message']= 'Not a valid password';
			}
			elseif($result == 4)
			{
				$response['error'] = true;
				$response['message']= 'Not a valid username';
			}
			
		}
		
		else
		{
			$response['error'] = true;
			$response['message']= 'Required fields are missing';
		}
	}
	else
	{
		$response['error'] = true;
		$response['message']= 'Invalid Request';
	}
	echo json_encode($response);
?>
<?php
	require_once'../includes/db_operations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$db = new DbOperations();
		
		//read input parameter
		$user_id = $_POST['user_id'];
		
		switch($_POST['list_type'])
		{
			case 'travel_list':
				//display all the items in the travel list
				$items = $db->DisplayTravelList($user_id);	
			if(empty($items))
				{
		
					$response['error'] = true;
					$response['message'] = "Nothing to display";
				}
				else
				{
					$response['error'] = false;
					$i=1;			
					foreach ($items as $item) :
					  $response["row"."$i"] = json_encode($item);
					  $i++;
					 endforeach;
				}
				
			break;
			
			case 'donation_list':
				//display all the items in the donation list
				$items = $db->DisplayDonationList($user_id);	
				if(empty($items))
				{
		
					$response['error'] = true;
					$response['message'] = "Nothing to display";
				}
				else
				{
					$response['error'] = false;
					$i=1;			
					foreach ($items as $item) :
					  $response["row"."$i"] = json_encode($item);
					  $i++;
					 endforeach;
				}
			break;
		
			default:
			break;
		}
	}
	echo json_encode($response);
?>
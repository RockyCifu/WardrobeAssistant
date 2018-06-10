<?php
	require_once'../includes/db_operations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$db = new DbOperations();
		
		//read input parameters
		$user_id = $_POST['user_id'];
		$item_id = $_POST['item_id'];
		switch($_POST['list_type'])
		{
			case 'travel_list':
				//check if item already present in the travel list
				if(false == $db->isItemExistsInTravel($item_id,$user_id))
				{
					//add item to the travel list
					$result = $db->addItemToTravelList($item_id,$user_id);
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Item added successfully';
					}
					elseif($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Some error occured please try again';
					}
				}
				else
				{
					$response['error'] = true;
					$response['message']= 'Item already present';
				}
				
				
			break;
			
			case 'donation_list':
			//check if item already present in the donation list
			if(false == $db->isItemExistsInDonation($item_id,$user_id))
				{
					//add item to the donation list
					$result = $db->addItemToDonationList($item_id,$user_id);
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Item added successfully';
					}
					elseif($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Some error occured please try again';
					}
				}
				else
				{
					$response['error'] = true;
					$response['message']= 'Item already present';
				}
			break;
						
			default:
			break;
		}
	}
	echo json_encode($response);
?>
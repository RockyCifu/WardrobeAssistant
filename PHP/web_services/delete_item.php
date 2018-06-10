<?php
	require_once'../includes/db_operations.php';
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$db = new DbOperations();
		
		//read the input parameters
		$delete_type = $_POST['delete_type'];
		$user_id = $_POST['user_id'];
		
		switch($delete_type)
		{
			case 'delete_item':
			//get the image path
			$image_path = $_POST['image_path'];
			//extract the item name
			$pos = strrpos($image_path, '/');
			$path = substr($image_path, $pos+1);
			//generate a path
			$path = '../uploads/'.$path;
			
			//unlink the path
			if(!unlink($path))
			{
				$response['error'] = true;
				$response['message'] = "Cannot delete";
			}
			else
			{
				//delete the item entry from the wardrobe table
				$result = $db->deleteItemFromWardrobe($image_path,$user_id);
					
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Successfully deleted';
					}
					else if($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Something wrong';
					}
			
			}
			
			break;
			case 'clear_travel_list':
			
				//Clear all the entries of travel list
				$result = $db->clearTravelList($user_id);
					
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Successfully deleted';
					}
					else if($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Something wrong';
					}
			break;
			
			case 'clear_donation_list':
				
				//Clear all the entries of donation list
				$result = $db->clearDonationList($user_id);
					
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Successfully deleted';
					}
					else if($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Something wrong';
					}
			break;
			case 'delete_outfit':
				
				//get the outfit name to be deleted
				$outfit_name = $_POST['outfit_name'];
				//delete all the entries of outfitlist corresponds to the outfit name
				$result = $db->deleteOutfit($outfit_name,$user_id);
					
					if($result == 1)
					{
						$response['error'] = false;
						$response['message']= 'Successfully deleted';
					}
					else if($result == 2)
					{
						$response['error'] = true;
						$response['message']= 'Something wrong';
					}
			break;
			
			default:
			break;
		}
	}
	echo json_encode($response);
?>
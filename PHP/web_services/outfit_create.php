<?php
	require_once'../includes/db_operations.php';
	$response = array();
	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		$db = new DbOperations();
		
		//read input parameters
		$operation = $_POST['operation'];
		$user_id = $_POST['user_id'];
		$outfit_name = $_POST['outfit_name'];
		
		switch($operation)
		{
			case 'validity': 
				//check if outfit name already exists
				$result = $db->isOutfitNameExists($outfit_name, $user_id);
				if($result == true)
				{
					$response['error'] = true;
					$response['message']= 'Outfit name already exists';
				}
				else
				{
					$response['error'] = false;
					$response['message']= 'Outfit name not exists';
				}
			break;
			
			case 'add':
				if(empty($outfit_name))
				{
					$response['error'] = true;
					$response['message']= 'Outfit not created';
				}
				else
				{
					$item_id = $_POST['item_id'];
					//check if the item already exists in the outfit
					if(false == $db->isItemidAlreadyExists($item_id,$outfit_name,$user_id))
					{
						//add item to outfit
						$result = $db->addItemToOutfitList($item_id,$outfit_name,$user_id);
						
						if($result == 1)
						{
							$response['error'] = false;
							$response['message']= 'Added successfully';
						}
						else if($result == 2)
						{
							$response['error'] = true;
							$response['message']= 'Something wrong';
						}
					}
					else
					{
						$response['error'] = true;
						$response['message']= 'Item already present';
					}
				}
			break;
						
			case 'read_outfit_names':
				//read all outfit names
				$items = $db->getOutfitNames($user_id);	
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
				 
			case 'read_outfit_items':
				//read all the items in the outfit 
				$items = $db->getOutfitItems($outfit_name,$user_id);	
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
<?php
	require_once'../includes/db_operations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST')
	{

		$db = new DbOperations();
		$user_id = $_POST['user_id'];
		
		switch($_POST['filter_type'])
		{
			case 'None': 
			{
				//display all the items in the wardrobe
				$items = $db->DisplayAll($user_id);	
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
			}
			case 'color':
			{ 
				//display all the items with slected color 
				$items = $db->DisplayByColor($_POST['filter_value'], $user_id);
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
			}
			case 'apparel_type':
			{
				//display all the items with the selected apprel type
				$items = $db->DisplayByApparelType($_POST['filter_value'], $user_id);	
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
				
			}
			case 'season':
			{
				//display all the items with slected season
				$items = $db->DisplayBySeason($_POST['filter_value'], $user_id);
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
			}
			
			case 'last_viewed':
			{
				//display all the items with the selected last_viewed date
				$items = $db->DisplayByDate($_POST['filter_value'], $user_id);
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
			}
			
			default:
				break;
		}			
	}
	echo json_encode($response);
?>
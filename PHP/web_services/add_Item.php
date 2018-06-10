<?php

	//Import database information
	require_once'../includes/db_operations.php';
	
	$response = array();
	
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		if(isset($_POST['user_id'])
			and isset($_POST['image'])
			and isset($_POST['location'])
			and isset($_POST['color']) 
			and isset($_POST['apparel_type']) 
			and isset($_POST['season'])){
		
			//Setting variables to reference the POST array data.
			$user_id = $_POST['user_id'];
			$color = $_POST['color'];
			$apparel_type = $_POST['apparel_type'];
			$season = $_POST['season'];
			$location = $_POST['location'];
			$image = $_POST['image'];
			
			$db = new DbOperations();
			
			$itemCount = $db->getNameCount($user_id,$color,$apparel_type);
			$itemCount++;
			
			//names the clothing item using tags
			$name = $color.$apparel_type.$itemCount;
			
			//String of url where picture is stored. 
			$imagePath = 'http://wardrobe-assistant.icoolshow.net/IT_project/uploads/'.$name.'.jpg';
			
			//Path where the picture will be decoded.
			$upload_path = '../uploads/'.$name.'.jpg';
			
			$result = $db->addItem($user_id,$imagePath,$color,$apparel_type,$season,$location);
				
			
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
			
			//Decodes the image and places file into upload_path destination.
			file_put_contents($upload_path,base64_decode($image));
		
		}
			echo json_encode($response);
						
	}else{
			echo 'paramaters not set';
		}

?>		
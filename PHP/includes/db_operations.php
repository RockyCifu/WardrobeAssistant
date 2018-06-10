<?php

	class DbOperations
	{
		private $con;
		
		function __construct()
		{
			require_once dirname(__FILE__).'/db_connect.php';
			
			$db = new DbConnect();
			
			$this->con = $db->connect();
		}
		
		/**
			createUser - Adds the new user to the users table
			@param: $username - user name
					$pass - password
			@return : 0 - if user already exists
					  1 - if user added successfully
					  2 - if any error in query execution
					  3 - if password does not meet the requirements
					  4 - if username does not meet the requirements
		*/
		public function createUser($username,$pass)
		{
			
			if($this->isUserExists($username))
			{
				return 0;
			}
			else
			{
				if(!$this->isValidUsername($username))
				{
					return 4;
				}
				
				if($this->isPasswordValid($pass))
				{
					$password = md5($pass);
					$stmt = $this->con->prepare("INSERT INTO `users` (`user_id`, `user_name`, `password`) VALUES (NULL, ?, ?);");
					$stmt->bind_param("ss",$username,$password);
					if($stmt->execute())
					{
						return 1;
					}
					else
					{
						return 2;
					}
				}
				else
				{
					return 3;
				}
			}

		}
		/**
			userLogin - if user is already created and tried to login
			@param: $username - user name
					$pass - password
			@return : number of rows returned upon execution of the query 
			
		*/
		public function userLogin($username, $pass)
		{
			$password = md5($pass);
			$stmt = $this->con->prepare("SELECT user_id FROM users WHERE user_name = ? AND password = ?");
			$stmt->bind_param("ss",$username,$password);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;	
		}
		
		/**
			getUserByUsername - get user information from user name and Send that information to android
			@param: $username - user name
			@return : rows returned upon execution of the query 
			
		*/
		public function getUserByUsername($username)
		{	
			$sql = "SELECT user_id, user_name FROM users WHERE user_name = '$username'";
			
			$result = $this->con->query($sql);
			return mysqli_fetch_array($result);
		}
		
		
		/**
			isUserExists - checks whether the user is already exixts in table
			@param: $username - user name
			@return : true - if user exists
					  false - if user not exists
			
		*/
		private function isUserExists($username)
		{
			$stmt = $this->con->prepare("SELECT user_id FROM users WHERE user_name = ?");
			$stmt->bind_param("s",$username);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;
		}
		/**
			isPasswordValid - checks whether the password meets the requirements
			@param: $password - password
			@return : true - if password valid
					  false - if password invalid
			
		*/
		private function isPasswordValid($password)
		{
			if (preg_match('/^\S{1,}$/',$password) )
			{
				if(strlen($password) > 7)
				{
					if(preg_match( '/[a-zA-Z]/', $password ))
					{
						if(preg_match( '/\d/', $password ))
						{
							if(preg_match('/[^a-zA-Z\d]/', $password));
							{
								return true;
							}
						}
					}
				}
			}
			return false;
		}
		/**
			isValidUsername - checks whether the username meets the requirements
			@param: $username - username
			@return : true - if username valid
					  false - if username invalid
			
		*/
		private function isValidUsername($username)
		{
			if (preg_match('/^\S{1,}$/',$username) )
			{
				if(strlen($username) > 1)
				{
					return true;
				}
			}
			return false;
		}
		/**
			addTagsForClothes - adds the clothes information to the database
			@param - $user_id - user id
					 $imagePath - location where image is stored in the server
					 $color - color of the dress
					 $apparel_type - apperel type of the dress
					 $season - season the dress is suitable to wear
					 $location - location of the dress
			@return		1 - if dress added successfully
						2 - if any error in query execution
			
		*/
		public function addItem($user_id, $imagePath,$color,$apparel_type,$season,$location)
		{
			
			$stmt = $this->con->prepare("INSERT INTO `wardrobe`(`item_id`, `user_id`, `image_path`, `color`, `apparel_type`, `season`, `location`, `last_viewed`) VALUES (NULL,?,?,?,?,?,?,now());");
			$stmt->bind_param("isssss",$user_id, $imagePath,$color,$apparel_type,$season,$location);
			return $stmt->execute() ? 1 : 2;
			
		}
		/**
			getNameCount - get the count of items with same name in the wardrobe table
			@param - $user_id - user id
					 $color - color of the dress
					 $apparel_type - apperel type of the dress
			@return	 count of items with same name 
			
		*/
		public function getNameCount($user_id,$color,$apparel_type){
			
			$sql = "SELECT image_path FROM wardrobe WHERE item_id = (SELECT MAX(item_id) FROM wardrobe) AND '$user_id' AND color = '$color' AND apparel_type = '$apparel_type'";
			
			$result = $this->con->query($sql);
			$numRows = $result->num_rows;
			
			if($numRows > 0){
				$sqlArray = mysqli_fetch_array($result);
				return substr($sqlArray['image_path'],-5,-4);
			}	
			else
				return 0;
			
		}
		
		/**
			DisplayAll - reads the all rows of the table wardrobe
			@param: none
			@return : result set
			
		*/
		public function DisplayAll($user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path,location FROM `wardrobe` WHERE user_id = $user_id";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;		
		}
		
		/**
			DisplayByColor - reads the all rows of the table wardrobe where color matches with the input color
			@param: color
			@return : result set
			
		*/
		public function DisplayByColor($color, $user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path,location FROM `wardrobe` WHERE color = '$color' AND user_id = $user_id";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		/**
			DisplayByApparelType - reads the all rows of the table wardrobe where apparel type matches with the input type
			@param: apparel_type
			@return : result set
			
		*/
		public function DisplayByApparelType($apparel_type, $user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path,location FROM `wardrobe` WHERE apparel_type = '$apparel_type' AND user_id = $user_id";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		
		/**
			DisplayBySeason - reads the all rows of the table wardrobe where season matches with the input season
			@param: season
			@return : result set
			
		*/
		public function DisplayBySeason($season, $user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path,location FROM `wardrobe` WHERE season = '$season' AND user_id = $user_id";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		
		/**
			DisplayByDate - reads the all rows of the table wardrobe where last_viewed date matches with the input date
			@param: date
			@return : result set
			
		*/
		public function DisplayByDate($date, $user_id)
		{
			$items = array();
			$item = strtotime($date);
			$newformat = date('Y-m-d',$item);
			$sql = "SELECT item_id,image_path,location FROM `wardrobe` WHERE last_viewed = '$newformat' AND user_id = $user_id";
			$result = $this->con->query($sql);

			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	

		}
	
		/**
			updateDate - update the last_viewed date
			@param: image_path
			@return : true - if sql execution is successful
					  false - if something wrong with sql
			
		*/
		public function updateDate($image_path,$user_id)
		{ 
	
		$query = "UPDATE wardrobe SET last_viewed = NOW() WHERE image_path= '$image_path' AND user_id = $user_id";

		$result = $this->con->query($query);
		return $result;
			
		}
		
		/**
			addItemToTravelList - add item to the travellist table
			@param: $user_id - user id
					 $item_id - item id
			@return : 1 - if sql execution is successful
					  2 - if something wrong with sql
			
		*/
		public function addItemToTravelList($item_id,$user_id)
		{
			$stmt = $this->con->prepare("INSERT INTO `travellist`(`id`,`item_id`, `user_id`) VALUES (NULL,?,?);");
			$stmt->bind_param("ii",$item_id,$user_id);
			return $stmt->execute() ? 1 : 2;			
		}
		
		/**
			addItemToDonationList - add item to the donationlist table
			@param: $user_id - user id
					 $item_id - item id
			@return : 1 - if sql execution is successful
					  2 - if something wrong with sql
			
		*/
		public function addItemToDonationList($item_id,$user_id)
		{
			$stmt = $this->con->prepare("INSERT INTO `donationlist`(`id`,`item_id`, `user_id`) VALUES (NULL,?,?);");
			$stmt->bind_param("ii",$item_id,$user_id);
			return $stmt->execute() ? 1 : 2;			
		}
		
		/**
			isOutfitNameExists - check if outfit name already exists in outfitlist table
			@param: $user_id - user id
					 $outfit_name - name of the outfit 
			@return : true - if outfit name already exists
					  false - if outfit name not exists
			
		*/
		public function isOutfitNameExists($outfit_name, $user_id)
		{
			$stmt = $this->con->prepare("SELECT * FROM outfitlist WHERE outfit_name = ? AND user_id = ?");
			$stmt->bind_param("si",$outfit_name,$user_id);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;
		}
		/**
			addItemToOutfitList - add items to outfitlist table
			@param: $user_id - user id
					 $outfit_name - name of the outfit 
					 $item_id - item id
			@return : 1 - if sql execution is successful
					  2 - If anything wrong with the sql
			
		*/
		public function addItemToOutfitList($item_id,$outfit_name, $user_id)
		{
			$stmt = $this->con->prepare("INSERT INTO `outfitlist`(`item_id`, `outfit_name`,`user_id`) VALUES (?,?,?);");
			$stmt->bind_param("isi",$item_id,$outfit_name,$user_id);
			return $stmt->execute() ? 1 : 2;	
			
		}
		/**
			DisplayTravelList - Display all the items in the travel list
			@param: $user_id - user id
			@return : result set
			
		*/
		public function DisplayTravelList($user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path FROM `wardrobe`WHERE item_id IN (SELECT item_id FROM travellist WHERE user_id = $user_id);";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		
		/**
			DisplayDonationList - Display all the items in the donationlist table
			@param: $user_id - user id
			@return : result set
			
		*/
		public function DisplayDonationList($user_id)
		{
			$items = array();
			$sql = "SELECT item_id,image_path FROM `wardrobe`WHERE item_id IN (SELECT item_id FROM donationlist WHERE user_id = $user_id);";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		
		/**
			getOutfitNames - Read all the outfit names in the outfitlist table
			@param: $user_id - user id
			@return : result set
			
		*/
		public function getOutfitNames($user_id)
		{
			$items = array();
			$sql = "SELECT distinct outfit_name FROM `outfitlist` WHERE user_id = $user_id";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		
		/**
			getOutfitItems - Display all the items in the outfitlist table for given outfit name
			@param: $user_id - user id
					$outfit_name - outfit name
			@return : result set
			
		*/
		public function getOutfitItems($outfit_name,$user_id)
		{

			$items = array();
			$sql = "SELECT item_id,image_path FROM `wardrobe`WHERE item_id IN (SELECT item_id FROM outfitlist WHERE outfit_name = '$outfit_name' AND user_id = $user_id);";
			$result = $this->con->query($sql);
			
			for ($i = 0; $i < $result->num_rows; $i++)
			{
				$item = $result->fetch_assoc();
				$items[] = $item;
			}
			
			return $items;	
		}
		/**
			isItemidAlreadyExists - Check if item is already in the outfit
			@param: $user_id - user id
					$outfit_name - outfit name
					$item_id - item id
			@return : true - If item already present in the outfit 
					  false - If item is not present in the outfit 
			
		*/
		public function isItemidAlreadyExists($item_id,$outfit_name,$user_id)
		{
			$stmt = $this->con->prepare("SELECT * FROM outfitlist WHERE outfit_name = ? AND user_id = ? AND item_id = ?");
			$stmt->bind_param("sii",$outfit_name,$user_id,$item_id);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;
		}
		
		/**
			deleteItemFromWardrobe - Delete the item from the wardrobe table
			@param: $user_id - user id
					$image_path - path of the image in the server
			@return : 1 - If sql execution is successful 
					  2 - If somethig wrong with the sql 
			
		*/
		public function deleteItemFromWardrobe($image_path,$user_id)
		{
			$stmt = $this->con->prepare("DELETE FROM `wardrobe` WHERE image_path = ? AND user_id = ?;");
			$stmt->bind_param("si",$image_path,$user_id);
			return $stmt->execute() ? 1 : 2;	
		}
		
		/**
			clearTravelList - Delete all the items from the travellist table
			@param: $user_id - user id
			@return : 1 - If sql execution is successful 
					  2 - If somethig wrong with the sql 
			
		*/
		public function clearTravelList($user_id)
		{
			$stmt = $this->con->prepare("DELETE FROM `travellist` WHERE user_id = ?;");
			$stmt->bind_param("i",$user_id);
			return $stmt->execute() ? 1 : 2;	
		}
		/**
			clearDonationList - Delete all the items from the donationlist table
			@param: $user_id - user id
			@return : 1 - If sql execution is successful 
					  2 - If somethig wrong with the sql 
			
		*/
		public function clearDonationList($user_id)
		{
			$stmt = $this->con->prepare("DELETE FROM `donationlist` WHERE user_id = ?;");
			$stmt->bind_param("i",$user_id);
			return $stmt->execute() ? 1 : 2;	
		}
		
		/**
			deleteOutfit - Delete all the items from the outfitlist table for given outfit name
			@param: $user_id - user id
					$outfit_name - outfit name
			@return : 1 - If sql execution is successful 
					  2 - If somethig wrong with the sql 
			
		*/
		public function deleteOutfit($outfit_name,$user_id)
		{
			$stmt = $this->con->prepare("DELETE FROM `outfitlist` WHERE user_id = ? AND outfit_name = ?;");
			$stmt->bind_param("is",$user_id,$outfit_name);
			return $stmt->execute() ? 1 : 2;	
		}
		/**
			isItemExistsInTravel - Check if the item is already present in the travellist
			@param: $user_id - user id
					$item_id - item id
			@return : true - if the item is already present 
					  fase - if the item is not present  
			
		*/
		public function isItemExistsInTravel($item_id,$user_id)
		{
			$stmt = $this->con->prepare("SELECT * FROM `travellist` WHERE user_id = ? AND item_id = ?");
			$stmt->bind_param("ii",$user_id,$item_id);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;
		}
		/**
			isItemExistsInDonation - Check if the item is already present in the donationlist
			@param: $user_id - user id
					$item_id - item id
			@return : true - if the item is already present 
					  fase - if the item is not present  
			
		*/
		public function isItemExistsInDonation($item_id,$user_id)
		{
			$stmt = $this->con->prepare("SELECT * FROM `donationlist` WHERE user_id = ? AND item_id = ?");
			$stmt->bind_param("ii",$user_id,$item_id);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows>0;
		}
		
	}
?>
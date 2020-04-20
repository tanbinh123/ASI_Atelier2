/**
 * Redirect if the user is not connected
 */
if(sessionStorage.getItem("Name") == null){
	window.location.href = "/login.html";
}



$(document).ready(function() {
	
	/**
	 *  Display homepage and set listeners for market display
	 */
	$("#market").hide();
	
	$('#homeIcon').click(function(){
		$("#market").hide();
		$("#home").show();
	});
	
	$('#sellIcon').click(function(){
		displaySellMarket();
	});
	
	$('#buyIcon').click(function(){
		displayBuyMarket();
	});
	
	
	$("#sellMarket").click(function(){
		displaySellMarket();
	});
	
	$("#buyButtonId").click(function(){
		displayBuyMarket();
	});
	
    //Adding information of the user
	var json = JSON.parse(sessionStorage.getItem("Name"));
	$("#userNameId").text(json.surname);
	$('#money').text(json.money + "$");
	

/*****************************************************************************************************************************************/
	
	
	/**
	 * Fill the selected image
	 * @param String imgUrl
	 * @param String name
	 * @param String description
	 * @param String attack
	 * @param String defence
	 * @param String price
	 * @param String id
	 */
	function fillImage(imgUrl, name, description, attack, defence, price, id){
		$('#cardImgId').attr("src", imgUrl);
	    $('#cardDescriptionId').text(description);
	    $('#Name').text(name);
	    $('#cardAttack').text(attack);
	    $('#cardDefence').text(defence);
	    $('#cardPriceId').text(price);
	    $('#cardId').text(id);
	}
	
	/**
	 * Add an image in the market list
	 * @param String imgUrl
	 * @param String name
	 * @param String description
	 * @param String attack
	 * @param String defence
	 * @param String price
	 * @param String id
	 */
	function addImage(imgUrl, name, description, attack, defence, price, id){
		$("#imgrow").append("<tr class = 'cardchild'><td>" +
                "<img  class='ui avatar image' src='"+ imgUrl +"'> <span class = 'Name'>"+ name +" </span>" +
            "</td>" +
            "<td class='Attack'>"+ attack +"</td>"+
            "<td class='Defence'>"+ defence +"</td>"+
            "<td class='Price'>"+ price +"</td>"+
            "<td class='Description' style='display:none !important'>" + description + "</td>" + 
            "<td class='Id' style='display:none !important'>" + id + "</td>" +
            "<td>"+
                "<div class='ui vertical animated button' tabindex='0'>"+
                    "<div class='hidden content'>"+ type +"</div>"+
                    "<div class='visible content'>"+
                        "<i class='shop icon'></i>"+
                    "</div>"+
                "</div>"+
            "</td></tr>");
	}
	
	/**
	 * Display all the cards in the array
	 * @param String[] array containing id of cards to display
	 */
	function displayCards(cards){
		var i;
		for(i = 0; i < cards.length; i++){
			
			  $.ajax({
				  url:"/CardService/" + cards[i],
				  type:"GET",
				  success: function( data ){
					  var json = JSON.parse(data);
					  fillImage(json.imgUrl, json.name, json.description, json.attack, json.defence, json.price, json.id);
					  addImage(json.imgUrl, json.name, json.description, json.attack, json.defence, json.price, json.id)
				  }
			  });
			  
		  }
	}
	
	/**
	 * Add a focus listener to fill the selected card
	 */
	function addFocusListener(){
		$("#imgrow").on("click", ".cardchild" ,function(){
			var imgUrl = $(this).find(".image").attr('src');
			var name = $(this).find(".Name").text();
			var description = $(this).find(".Description").text();
			var attack = $(this).find(".Attack").text();
			var defence = $(this).find(".Defence").text();
			var price = $(this).find(".Price").text();
			var id = $(this).find(".Id").text();
		    fillImage(imgUrl, name, description, attack, defence, price, id);
		});
	}
	
	/**
	 * Add listener to buy or sell a card
	 * @param String Sell or Buy
	 */
	function addClickListener(type){
		$("#OrderSelected").on("click", function(){
			confirm("Would you really " + type + " the selected card ?");
			var id = $("#cardId").text();
			var price = $("#cardPriceId").text();
			
			$.ajax({
				  url:"/UserService/" + type + "/" + json.name + "/" + id + "/" + price,
				  type:"GET",
				  success: function( data ){
					  if(data == true){
						  //Erase the card in the market
						  $("td").filter(function() {
							    return $(this).text() == id;
							}).closest("tr").attr("style", "display: none !important"); 
					  }
				  }
			  });
			
			
		});	
	}
	
	
		
	function displaySellMarket(){
		
		$("#imgrow").empty();
		
		
		type = "Sell";
		$('#marketType')[0].innerText=type;
		
		$("#home").hide();
		$("#market").show();
		
		//First AJAX call to get the cards of the user
		$.ajax({
			  url:"/UserService/user/cards/" + json.id,
			  type:"GET",
			  success: function( data ){
				  var cards = data.split("/");
				  displayCards(cards);
				  addFocusListener();
				  addClickListener("Sell");
			  }
			  
		});
		 
	}
	
	
	
	function displayBuyMarket(){

		$("#imgrow").empty();
		
		type = "Buy";
		$('#marketType')[0].innerText=type;
		
		$("#home").hide();
		$("#market").show();
		
		
		$.ajax({
			  url:"UserService/user/BuyCards/" + json.id,
			  type:"GET",
			  success: function( data ){
				  cards = data.split('/');
				  displayCards(cards);
				  addFocusListener();
				  addClickListener("Buy");
			  }
		  });
	}
	
	
});
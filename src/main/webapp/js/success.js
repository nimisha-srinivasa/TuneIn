$.urlParam = function(name){
	var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	return results[1] || 0;
}

$(document).ready(function(){
    audioclip_key = $.urlParam('audioclip_key');
    userId = $.urlParam('userId');
    basic_url = "/rest/users/"+userId+"/audioClips/";
     $.ajax({
            url: basic_url+audioclip_key,
            type: "GET",
            crossDomain: true,
            dataType: "json",
            success: function (data) {
            	image_url = basic_url+"image?blobkey="+data.imageId;
            	audio_url = basic_url+"audio?blobkey="+data.audioId;
//                $('#myTable').append('<tr><td>' + data.title + '</td></tr>');
//		        $('#myTable').append('<tr><td> 	<img src=\"'+image_url+'\" /> </td></tr>');
//		        $('#myTable').append('<tr><td>' + data.date + '</td></tr>');
//		        $('#myTable').append('<tr><td><audio controls> <source src=\"'+audio_url+'\" type="audio/mpeg" /></audio></td></tr>');
		        $('#successData').append('<div class="col-md-4 col-sm-6 portfolio-item"><a href="#successModal'+audioclip_key+'" class="portfolio-link" data-toggle="modal" data-target="#successModal'+audioclip_key+'"><div class="portfolio-hover"><div class="portfolio-hover-content"><i class="fa fa-youtube-play fa-3x"></i></div></div><img src="'+image_url+'" class="audio_image" alt=""></a><div class="portfolio-caption"><h4>'+data.title+'</h4>data.title</div>');
		        $('#successModal').append('<div class="audio-modal modal fade" id="successModal'+audioclip_key+'" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-body"><a class="audio-modal close-modal" data-dismiss="modal"><i class="fa fa-3x fa-times"></i></a><h2>'+data.title+'</h2><img class="portfolio-img img-responsive img-centered" src="'+image_url+'" alt=""><br/><audio controls><source src="'+audio_url+'" type="audio/mpeg" /></audio><br/><br/><ul class="list-inline"><li>Date: '+data.date+'</li></ul><br/></div><div class="clearfix"></div></div></div></div></div>');
            },
            error: function (xhr, status) {
                alert("error");
            }
        });
});
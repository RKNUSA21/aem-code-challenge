$(document).on("click", "#formsubmit", function (e) {

    $.ajax({

        type: 'POST',

        url:'/bin/saveUserDetails',

        data:{'firstName' : $('#firstName').val(),'lastName' : $('#lastName').val(),'country' : $('#country')[0].innerText,'age' : $('#age').val()},          

        success: function(msg){

            //Success logic here(The response from servlet)
        },
        error:function(msg){
            $('#errorMsg').show();
        }
    });

});


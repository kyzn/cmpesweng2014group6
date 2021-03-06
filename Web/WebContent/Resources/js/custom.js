
$(document).ready(function() {
	errorCheck();
	successCheck();

	$(function() {
		if($('input[name=genderHidden]').val()=="Male"){
			$("#editGenderSelect").val('Male');
		}else if($('input[name=genderHidden]').val()=="Female"){
			$("#editGenderSelect").val('Female');
		}else{
			$("#editGenderSelect").val('Unspecified');
		}
	});

	$('#addStoryForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			placeName: {
				message: 'Place name is not valid',
				validators: {
					notEmpty: {
						message: 'Place name is required and cannot be empty'
					},
					stringLength: {
						min: 3,
						max: 30,
						message: 'Place name must be more than 3 and less than 30 characters long'
					}
				}
			},
			editStory: {
				message: 'Story is not valid',
				validators: {
					notEmpty: {
						message: 'Story is required and cannot be empty'
					}
				}
			},
			editStime: {
				message: 'Time is not valid',
				validators: {
					notEmpty: {
						message: 'Time is required and cannot be empty'
					}
				}
			},
			tags: {
				message: 'Tag format is not valid',
				validators: {
					regexp : {
						regexp : /^([a-zA-Z0-9._%+-]+(\s)*[a-zA-Z0-9._%+-]*)(,(\s)*[a-zA-Z0-9._%+-]+(\s)*[a-zA-Z0-9._%+-]*)*$/i,
						message : 'Tag format is not valid'
					}
				}
			}
		}
	});

	$('#registerForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			name: {
				message: 'The name is not valid',
				validators: {
					notEmpty: {
						message: 'The name is required and cannot be empty'
					},
					stringLength: {
						min: 3,
						max: 30,
						message: 'The name must be more than 3 and less than 30 characters long'
					},
					regexp: {
						regexp: /^[a-z ,.'-]+$/i,
						message: 'The name is not valid'
					},
					different: {
						field: 'password',
						message: 'The name and password cannot be the same as each other'
					}
				}
			},
			email: {
				validators: {
					notEmpty: {
						message: 'The email address is required and cannot be empty'
					},
					
					
					regexp:
					{
						
						regexp: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[A-Za-z]{2,4}$/i,
						message: 'The email address is not a valid'
					}
//This allows qwe@qwe, which we don't want
//					emailAddress: {
//						message: 'The email address is not a valid'
//					}
				}
			},
			password: {
				validators: {
					notEmpty: {
						message: 'The password is required and cannot be empty'
					},
					different: {
						field: 'username',
						message: 'The password cannot be the same as username'
					},
					stringLength: {
						min: 6,
						message: 'The password must have at least 6 characters'
					}
				}
			}
		}
	});

	/*
	 * This button already of type "submit"
	 * If you uncomment this, you'll enter doPost twice
	 * which will try to register two times. First one will succeed,
	 * second one will return "user already registered."
	 * 
	 * $("#registerButton").click(function(){
		var $form = $("#registerForm");
		$.ajax({
			type: "POST",
			url: $form.attr("action"),
			data: $form.serialize(),

		});
	});
*/

	$('#loginForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			email: {
				validators: {
					notEmpty: {
						message: 'The email address is required and cannot be empty'
					},
					emailAddress: {
						message: 'The email address is not valid'
					}
				}
			},
			password: {
				validators: {
					notEmpty: {
						message: 'The password is required and cannot be empty'
					},
					stringLength: {
						min: 6,
						message: 'The password must have at least 6 characters'
					}
				}
			}
		}
	});

	$('#changePasswordForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			oldPassword: {
				validators: {
					notEmpty: {
						message: 'The password is required and cannot be empty'
					},
					stringLength: {
						min: 6,
						message: 'The password must have at least 6 characters'
					}
				}
			},
			newPassword: {
				validators: {
					notEmpty: {
						message: 'The password is required and cannot be empty'
					},
					stringLength: {
						min: 6,
						message: 'The password must have at least 6 characters'
					}
				}
			},
			reNewPassword: {
				validators: {
					notEmpty: {
						message: 'The password is required and cannot be empty'
					},
					stringLength: {
						min: 6,
						message: 'The password must have at least 6 characters'
					},
					identical: {
						field: 'newPassword',
						message: 'New password fields do not match.'
					}
				}
			}
		}
	});

	$('#editProfileForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			editPhone: {
				validators: {
					regexp:
					{
						regexp: /^5\d{9}/,
						message: 'The phone number is not valid'
					},
					stringLength: {
						max: 10,
						message: 'The phone number must have 10 digits'
					}
				}
			},
			editBirthdate : {
				validators: {
					date: {
						format: 'DD/MM/YYYY',
						message: 'The date of birth is not valid'
					}
				}
			},
			editName: {
				validators: {
					notEmpty: {
						message: 'The name is required and cannot be empty'
					},
					stringLength: {
						min: 3,
						max: 30,
						message: 'The name must be more than 3 and less than 30 characters long'
					},
					regexp: {
						regexp: /^[a-z ,.'-]+$/i,
						message: 'The name is not valid'
					}
				}
			},
			editBio: {
				validators: {
					stringLength: {
						max: 140,
						message: 'Your bio cannot be longer than 140 chars.'
					}
				}
			}
		}
	});

	$("#loginButton").click(function(e){
		var $form = $("#loginForm");
		$.ajax({
			type: "POST",
			url: "Login",
			data: $form.serialize()
		});
	});

	$("#editBirthdate").datepicker({
		format: "dd/mm/yyyy"
	});

	
	

});

function errorCheck()
{
	if($('input[name=error]').val()=="true"){
		$('#errorPop').modal('show');
		$('input[name=error]').val("false");
	}
}

function successCheck()
{
	if($('input[name=success]').val()=="true"){
		$('.editSuccess').modal('show');
		$('input[name=success]').val("false");
	}
}

function logOut(){
	$.ajax({
		type: "POST",
		url: "Login",
		data:
		{
			func: "logout"
		},
		success: function(data)
		{
			window.location = "index.jsp"
		}
	})
}

function goToEdit()
{
	$.ajax({
		type: "GET",
		url: "Login",
		data:
		{
			func: "gotoedit"
		}
	})
}



	

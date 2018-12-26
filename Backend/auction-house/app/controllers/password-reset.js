import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import { isEmpty } from '@ember/utils';
import swal from 'sweetalert';

export default Controller.extend({

  resetSuccess: null,
  resetText: null,

  emailIsValid: null,
  emailErrorText: null,

  actions: {
    reset: function() {
      var _this = this;
      var regex = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
      
      if(!(isEmpty(this.get('emailAddress')))) {
        if(regex.test(this.get('emailAddress'))) {
          _this.set('emailIsValid', true);
          $.ajax({
            url: ENV.HOST_URL+'/api/v1/reset',
            type: 'POST',
            data: JSON.stringify({
              email: this.get('emailAddress')
            }),
            contentType: 'application/json;charset=utf-8',
            dataType: 'json'
          }).then(function(){
            swal("Email Sent!", "A reset mail has been sent to your account!", "success");
          }).catch(function(){
            swal("Ooops!", "It would seem that mail doesn't exist. Please try again!", "error");
          });
        } else {
          _this.set('emailIsValid', false);
          _this.set('emailErrorText', "Invalid E-mail account");
        }
      } else {
        _this.set('emailIsValid', false);
        _this.set('emailErrorText', "E-mail can not be empty");
      }
    },

    clearFields: function(){
      this.set('resetSuccess', null);
      this.set('resetText', null);
      this.set('emailIsValid', null);
      this.set('emailErrorText', null);
      this.set('emailAddress', ''); 
    }
  }
});

import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { isEqual } from '@ember/utils';
import { inject as service } from '@ember/service';
import $ from 'jquery';
import swal from 'sweetalert';
import ENV from 'auction-house/config/environment';

export default Controller.extend({

  loadingSlider: service(),

  resetSuccess: null,
  resetText: null,

  passwordIsValid: null,
  passwordErrorText: null,

  passwordMatches: null,
  passwordMatchesErrorText: null,

  token: null,

  actions: {
    async reset() {
      var _this = this;
      var regex = new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{4,64}$/);
      if(!(isEmpty(this.get('password')))){
        if((this.get('password').length > 3)){
          if(regex.test(this.get('password'))){
            this.set('passwordIsValid', true);
            if(isEqual(this.get('password'), this.get('confirmPassword'))){
              this.set('passwordMatches', true);
              _this.get('loadingSlider').startLoading();
              await $.ajax({
                url: ENV.HOST_URL+'/api/v1/password/reset',
                type: 'POST',
                data: JSON.stringify({
                  password: this.get('password')
                }),
                headers: {
                  'X-AUTH-TOKEN': _this.get('token')
                },
                contentType: 'application/json;charset=utf-8',
                dataType: 'json'
              }).then(function(){
                _this.get('loadingSlider').endLoading();
                swal("Password Reset!", "You have successfully reset your password!", "success");
                _this.transitionToRoute('login');
              }).catch(function(){
                _this.get('loadingSlider').endLoading();
                swal("Ooops!", "It would seem an error has occurred please try again.", "error");
                _this.transitionToRoute('home');
              });
            } else {
              this.set('passwordMatches', false);
              this.set('passwordMatchesErrorText', 'Passwords do not match');
            }
          } else {
            this.set('passwordIsValid', false);
            this.set('passwordErrorText', 'Password must include at least one upper case letter, one lower case letter, and a number');
          }
        } else {
          this.set('passwordIsValid', false);
          this.set('passwordErrorText', 'Password is too short (minimum is 4 characters)');
        }
      } else {
        this.set('passwordIsValid', false);
        this.set('passwordErrorText', 'Password can not be empty');
      }
    },

    clearFields: function(){
      this.set('resetSuccess', null);
      this.set('resetText', null);
      this.set('passwordIsValid', null);
      this.set('passwordErrorText', null);
      this.set('passwordMatches', null);
      this.set('passwordMatchesErrorText', null);
      this.set('token', null);
      this.set('password', '');
      this.set('confirmPassword', '');
    }
  }
});

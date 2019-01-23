import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { isEmpty } from '@ember/utils';

export default Controller.extend({ 
  store: service(),
  loadingSlider: service(),
  
  currentDate: moment(new Date()).format("DD/MM/YYYY"),
  currentDatePlaceHolder: 'e.g. ' + moment(new Date()).format("DD/MM/YYYY"),
  progress: 0,

  selectedOption: null,

  hasError: null,
  newPasswordToggled: false,

  nameHasError: null,
  nameErrorMessage: null,

  surnameHasError: null,
  surnameErrorMessage: null,

  passwordErrorMessage: null,
  passwordHasError: null,

  dateOfBirthHasError: null,
  dateOfBirthErrorMessage: null,

  phoneNumberHasError: null,
  phoneNumberErrorMessage: null,

  streetHasError: null,
  streetErrorMessage: null,

  cityHasError: null,
  cityErrorMessage: null,

  zipCodeHasError: null,
  zipCodeErrorMessage: null,

  stateHasError: null,
  stateErrorMessage: null,

  countryHasError: null,
  countryErrorMessage: null,

  customValidation: function(){
    var checker = true;
    
    if(!(isEmpty(this.get('phoneNumber')))) {
      if(this.get('phoneNumber').length != 0 && this.get('phoneNumber').trim().length == 0) {
        this.set('phoneNumberHasError', true);
        this.set('phoneNumberErrorMessage', 'Phone can not be blank');
        checker = false;
      } else {
        this.set('streetHasError', false);
      }
    }

    if(!(isEmpty(this.get('street')))) {
      if(this.get('street').length != 0 && this.get('street').trim().length == 0) {
        this.set('streetHasError', true);
        this.set('streetErrorMessage', 'Street can not be blank');
        checker = false;
      } else {
        this.set('streetHasError', false);
      }
    }

    if(!(isEmpty(this.get('city')))) {
      if(this.get('city').length != 0 && this.get('city').trim().length == 0) {
        this.set('cityHasError', true);
        this.set('cityErrorMessage', 'City can not be blank');
        checker = false;
      } else {
        this.set('cityHasError', false);
      }
    }

    if(!(isEmpty(this.get('zipCode')))) {
      if(this.get('zipCode').length != 0 && this.get('zipCode').trim().length == 0) {
        this.set('zipCodeHasError', true);
        this.set('zipCodeErrorMessage', 'ZipCode can not be blank');
        checker = false;
      } else {
        this.set('zipCodeHasError', false);
      }
    }

    if(!(isEmpty(this.get('state')))) {
      if(this.get('state').length != 0 && this.get('state').trim().length == 0) {
        this.set('stateHasError', true);
        this.set('stateErrorMessage', 'State can not be blank');
        checker = false;
      } else {
        this.set('stateHasError', false);
      }
    }

    if(!(isEmpty(this.get('country')))) {
      if(this.get('country').length != 0 && this.get('country').trim().length == 0) {
        this.set('countryHasError', true);
        this.set('countryErrorMessage', 'Country can not be blank');
        checker = false;
      } else {
        this.set('countryHasError', false);
      }
    }  
    return checker;

  },

  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },

    async updateUser() {
      var _this = this;
      var bd;

      let user = this.get('model.user');
      let address = this.get('model.user.address');

      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));

      if(this.get('newPasswordToggled')) {
        user.set('password', this.get('password'));
        user.set('passwordConfirmation', this.get('password'));
        user.set('new_password', true);
      } else {
        user.set('password', "PlaceHolder123!@#");
        user.set('passwordConfirmation', "PlaceHolder123!@#");
        user.set('new_password', false);
      }

      user.set('emailConfirmation', user.get('email'));
      user.set('gender', this.get('selectedOption'));

      if(!isEmpty(this.get('dateOfBirth'))) {
        bd = new Date(this.get('dateOfBirth'));
        bd.setMinutes(bd.getMinutes() - bd.getTimezoneOffset());
        user.set('dateOfBirth', bd);  
      } else {
        user.set('dateOfBirth', null);
      }
      
      user.set('phoneNumber', this.get('phoneNumber'));
   
      address.set('street', this.get('street'));
      address.set('city', this.get('city'));
      address.set('zipCode', this.get('zipCode'));
      address.set('state', this.get('state'));
      address.set('country', this.get('country'));

      await user.validate().then(({ validations }) => {
        if(validations.get('isValid')) {
          if(_this.customValidation()) {
            _this.get('loadingSlider').endLoading();
            _this.get('loadingSlider').startLoading();
            user.save().then(function() {
              _this.set('nameHasError', null);
              _this.set('surnameHasError', null);
              _this.set('passwordHasError', null);
              _this.set('dateOfBirthHasError', null);
              _this.set('phoneNumberHasError', null);
              _this.set('streetHasError', null);
              _this.set('cityHasError', null);
              _this.set('zipCodeHasError', null);
              _this.set('stateHasError', null);
              _this.set('countryErrorMessage', null);
              _this.get('loadingSlider').endLoading();
              swal("Success!", "You've successfully updated your profile!", "success");
            }).catch(function(){
              _this.get('loadingSlider').endLoading();
              swal("Ooops!", "It would seem an error has occurred please try again.", "error");
            });
          }
        } else {

          if(user.get('validations.attrs.name.messages') !== '' && user.get('validations.attrs.name.messages') !== null){
            this.set('nameHasError', true);
            this.set('nameErrorMessage', user.get('validations.attrs.name.messages'));
          }
          
          if(user.get('validations.attrs.surname.messages') !== '' && user.get('validations.attrs.surname.messages') !== null){
            this.set('surnameHasError', true);
            this.set('surnameErrorMessage', user.get('validations.attrs.surname.messages'));
          }

          if(user.get('validations.attrs.password.messages') !== '' && user.get('validations.attrs.password.messages') !== null) {
            this.set('passwordHasError', true);
            this.set('passwordErrorMessage', user.get('validations.attrs.password.messages'));
          }
          
          if(user.get('validations.attrs.dateOfBirth.messages') !== '' && user.get('validations.attrs.dateOfBirth.messages') !== null){
            this.set('dateOfBirthHasError', true);
            this.set('dateOfBirthErrorMessage', user.get('validations.attrs.dateOfBirth.messages'));
          }            

          if(user.get('validations.attrs.phoneNumber.messages') !== '' && user.get('validations.attrs.phoneNumber.messages') !== null){
            this.set('phoneNumberHasError', true);
            this.set('phoneNumberErrorMessage', user.get('validations.attrs.phoneNumber.messages'));
          }
          
        }
      });
    },

    async imageUploadComplete(info) {
      var _this = this;
      let user = this.get('model.user');
      user.set('avatar', info.image);
      await user.save().then(function(){
        if(_this.get('progress') == 1){
          _this.set('progress', 0);
        }
      });
    },

    imageUploadLoading: function(value) {
      var _this = this;
      Ember.run.once(function(){
        _this.set('progress', (value.percent/100));
      });
    },

    toggleNewPassword: function() {
      this.set('newPasswordToggled', true);
      this.set('password', '');
    },

    clearFields: function() {
      this.set('newPasswordToggled', false);
      this.set('password', '');
      this.set('nameHasError', null);
      this.set('nameErrorMessage', null);
      this.set('surnameHasError', null);
      this.set('surnameErrorMessage', null);
      this.set('passwordHasError', null);
      this.set('passwordErrorMessage', null);
      this.set('dateOfBirthHasError', null);
      this.set('dateOfBirthErrorMessage', null);
      this.set('phoneNumberHasError', null);
      this.set('phoneNumberErrorMessage', null);
      this.set('streetHasError', null);
      this.set('streetErrorMessage', null);
      this.set('cityHasError', null);
      this.set('cityErrorMessage', null);
      this.set('zipCodeHasError', null);
      this.set('zipCodeErrorMessage', null);
      this.set('nameHasError', null);
      this.set('nameHasError', null);
      this.set('stateHasError', null);
      this.set('stateErrorMessage', null);
      this.set('countryHasError', null);
      this.set('countryErrorMessage', null);
    }

  }
});

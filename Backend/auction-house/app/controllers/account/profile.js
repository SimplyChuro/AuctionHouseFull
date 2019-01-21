import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';

export default Controller.extend({ 
  store: Ember.inject.service(),
  selectedOption: null,

  hasError: null,

  nameHasError: null,
  nameErrorMessage: null,

  surnameHasError: null,
  surnameErrorMessage: null,

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

    if(this.get('phoneNumber').length != 0 && this.get('phoneNumber').trim().length == 0) {
      this.set('phoneNumberHasError', true);
      this.set('phoneNumberErrorMessage', 'Phone can not be blank');
      checker = false;
    } else {
      this.set('streetHasError', false);
    }

    if(this.get('street').length != 0 && this.get('street').trim().length == 0) {
      this.set('streetHasError', true);
      this.set('streetErrorMessage', 'Street can not be blank');
      checker = false;
    } else {
      this.set('streetHasError', false);
    }

    if(this.get('street').length != 0 && this.get('street').trim().length == 0) {
      this.set('streetHasError', true);
      this.set('streetErrorMessage', 'Street can not be blank');
      checker = false;
    } else {
      this.set('streetHasError', false);
    }

    if(this.get('city').length != 0 && this.get('city').trim().length == 0) {
      this.set('cityHasError', true);
      this.set('cityErrorMessage', 'City can not be blank');
      checker = false;
    } else {
      this.set('cityHasError', false);
    }

    if(this.get('zipCode').length != 0 && this.get('zipCode').trim().length == 0) {
      this.set('zipCodeHasError', true);
      this.set('zipCodeErrorMessage', 'ZipCode can not be blank');
      checker = false;
    } else {
      this.set('zipCodeHasError', false);
    }

    if(this.get('state').length != 0 && this.get('state').trim().length == 0) {
      this.set('stateHasError', true);
      this.set('stateErrorMessage', 'State can not be blank');
      checker = false;
    } else {
      this.set('stateHasError', false);
    }

    if(this.get('country').length != 0 && this.get('country').trim().length == 0) {
      this.set('countryHasError', true);
      this.set('countryErrorMessage', 'Country can not be blank');
      checker = false;
    } else {
      this.set('countryHasError', false);
    }
    
    return checker;

  },

  actions: {
    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },

    updateUser: function() {
      let selectedOption = this.get('selectedOption');
      var _this = this;

      let user = this.get('model.user');
      let address = this.get('model.user.address');

      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('password', "Temporary123!@#");
      user.set('passwordConfirmation', "Temporary123!@#");
      user.set('emailConfirmation', user.get('email'));
      user.set('gender', this.get('selectedOption'));

      var bd = new Date(this.get('dateOfBirth'));
      bd.setMinutes(bd.getMinutes() - bd.getTimezoneOffset());

      if(!isEmpty(this.get('dateOfBirth'))){
        user.set('dateOfBirth', bd);  
      } else {
        user.set('dateOfBirth', null);
      }
      
      user.set('phoneNumber', this.get('phoneNumber'));
      user.set('bids', []);
      user.set('sales', []);
      user.set('reviews', []);
      user.set('wishlist', []);
   
      address.set('street', this.get('street'));
      address.set('city', this.get('city'));
      address.set('zipCode', this.get('zipCode'));
      address.set('state', this.get('state'));
      address.set('country', this.get('country'));

      user.validate().then(({ validations }) => {
        if(validations.get('isValid')) {
          if(_this.customValidation()){
            user.save().then(function(){
              _this.set('hasError', false);
              _this.get('flashMessages').success('Updated Profile!');

              _this.set('nameHasError', null);
              _this.set('surnameHasError', null);
              _this.set('dateOfBirthHasError', null);
              _this.set('phoneNumberHasError', null);
              _this.set('streetHasError', null);
              _this.set('cityHasError', null);
              _this.set('zipCodeHasError', null);
              _this.set('stateHasError', null);
              _this.set('countryErrorMessage', null);
            
            }).catch(function(){
              _this.set('hasError', true);
              _this.get('flashMessages').warning('Oops! An unexpected error occoured.');
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
          
          if(user.get('validations.attrs.dateOfBirth.messages') !== '' && user.get('validations.attrs.dateOfBirth.messages') !== null){
            this.set('dateOfBirthHasError', true);
            this.set('dateOfBirthErrorMessage', user.get('validations.attrs.dateOfBirth.messages'));
          }            

          if(user.get('validations.attrs.phoneNumber.messages') !== '' && user.get('validations.attrs.phoneNumber.messages') !== null){
            this.set('phoneNumberHasError', true);
            this.set('phoneNumberErrorMessage', user.get('validations.attrs.phoneNumber.messages'));
          }
          
          _this.set('hasError', true); 
          _this.get('flashMessages').warning('Oops! An unexpected error occoured.');
        }
      });
    }
  }
});

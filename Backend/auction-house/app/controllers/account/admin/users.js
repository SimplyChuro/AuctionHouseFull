import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { inject as service } from '@ember/service';
import swal from 'sweetalert';

export default Controller.extend({
  store: service(),
  loadingSlider: service(),
  
  currentDate: moment(new Date()).format("DD/MM/YYYY"),
  currentDatePlaceHolder: 'e.g. ' + moment(new Date()).format("DD/MM/YYYY"),

  currentUser: null,
  userCreateEnabled: false,
  userEditEnabled: false,
  
  selectedOption: null,

  hasError: null,

  nameHasError: null,
  nameErrorMessage: null,

  surnameHasError: null,
  surnameErrorMessage: null,

  emailErrorMessage: null,
  emailHasError: null,

  emailConfirmationErrorMessage: null,
  emailConfirmationHasError: null,

  passwordErrorMessage: null,
  passwordHasError: null,

  passwordConfirmationErrorMessage: null,
  passwordConfirmationHasError: null,

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

  emailExistsErrorMessage: 'The input email is in usage',
  emailExistsHasError: null,

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

    async createUser() {    
      let user = this.store.createRecord('user');
      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('email', this.get('email'));
      user.set('password', this.get('password'));
      user.set('emailConfirmation', this.get('email'));
      user.set('passwordConfirmation', this.get('password'));

      var _this = this;
      await user.validate().then(({ validations }) =>{
        if(validations.get('isValid')){
          _this.get('loadingSlider').endLoading();
          _this.get('loadingSlider').startLoading();
          _this.set('emailExistsHasError', null);
          _this.set('nameHasError', null);
          _this.set('surnameHasError', null);
          _this.set('emailHasError', null);
          _this.set('emailConfirmationHasError', null);
          _this.set('passwordHasError', null);
          _this.set('passwordConfirmationHasError', null);

          user.set('address', null);

          user.save().then(function() {
            _this.set('userCreateEnabled', false);
            _this.get('loadingSlider').endLoading();
            swal("Success!", "User Successfully Made", "success");
          }).catch(function() {
            _this.set('emailExistsHasError', true);
            _this.set('nameHasError', null); 
            _this.set('nameErrorMessage', null);

            _this.set('surnameHasError', null); 
            _this.set('surnameErrorMessage', null);

            _this.set('emailHasError', null); 
            _this.set('emailErrorMessage', null);

            _this.set('passwordHasError', null); 
            _this.set('passwordErrorMessage', null);
            _this.get('loadingSlider').endLoading();
            user.destroyRecord();
            swal("Ooops!", "It would seem an error has occurred please try again.", "error");
          });
        } else {

          if(user.get('validations.attrs.name.messages') !== '' && user.get('validations.attrs.name.messages') !== null) {
            this.set('nameHasError', true);
            this.set('nameErrorMessage', user.get('validations.attrs.name.messages'));
          }
          
          if(user.get('validations.attrs.surname.messages') !== '' && user.get('validations.attrs.surname.messages') !== null) {
            this.set('surnameHasError', true);
            this.set('surnameErrorMessage', user.get('validations.attrs.surname.messages'));
          }
          
          if(user.get('validations.attrs.email.messages') !== '' && user.get('validations.attrs.email.messages') !== null) {
            this.set('emailHasError', true);
            this.set('emailErrorMessage', user.get('validations.attrs.email.messages'));
          }

          if(user.get('validations.attrs.password.messages') !== '' && user.get('validations.attrs.password.messages') !== null) {
            this.set('passwordHasError', true);
            this.set('passwordErrorMessage', user.get('validations.attrs.password.messages'));
          }

          user.destroyRecord();

        }
      })
    },

    async updateUser(){
      var _this = this;
      var bd;

      let user = this.get('currentUser');
      let address = this.get('currentUser.address');

      user.set('name', this.get('name'));
      user.set('surname', this.get('surname'));
      user.set('password', "PlaceHolder123!@#");
      user.set('passwordConfirmation', "PlaceHolder123!@#");
      user.set('email', user.get('email'));
      user.set('emailConfirmation', user.get('email'));
      
      if(!isEmpty(this.get('selectedOption'))){
        user.set('gender', this.get('selectedOption'));
      }

      if(!isEmpty(this.get('dateOfBirth'))){
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

      user.validate().then(({ validations }) => {
        console.log(validations.get('errors'));
        if(validations.get('isValid')) {
          if(_this.customValidation()){
            _this.get('loadingSlider').endLoading();
            user.save().then(function(){
              _this.get('loadingSlider').endLoading();
              _this.set('nameHasError', null);
              _this.set('surnameHasError', null);
              _this.set('dateOfBirthHasError', null);
              _this.set('phoneNumberHasError', null);
              _this.set('streetHasError', null);
              _this.set('cityHasError', null);
              _this.set('zipCodeHasError', null);
              _this.set('stateHasError', null);
              _this.set('countryErrorMessage', null); 
              swal("Success!", "User Successfully Updated", "success");
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


    async deleteUser() {
      var _this = this;
      let user = this.get('currentUser');
      user.destroyRecord().then(function(){
        _this.set('currentUser', null);
        _this.set('userCreateEnabled', false);
        _this.set('userEditEnabled', false);
      });
    },

    async imageUploadComplete(info) {
      var _this = this;
      let user = this.get('currentUser');
      user.set('avatar', info.image)
      user.save().then(function(){
        if(_this.get('progress') == 1){
          _this.set('progress', 0);
        }
      });
    },

    async imageUploadLoading(value) {
      var _this = this;
      Ember.run.once(function(){
        _this.set('progress', (value.percent/100));
      });
    },

    async deleteUserPicture() {
      var _this = this;
      let user = this.get('currentUser');
      user.set('avatar', null);
      user.save().then(function(){
        
      });
    },

    toggleDetails: function(user) {
      var checker = this.get('currentUser');

      if((checker === null) || (user.id !== checker.id)) {
        this.set('userEditEnabled', false);
        this.set('userCreateEnabled', false);
        this.set('currentUser', user);
      } else {
        this.set('currentUser', null);
      }

    },

    toggleUserCreate: function() {
      this.set('userEditEnabled', false);
      this.set('userCreateEnabled', true);
      this.set('currentUser', null);
      this.set('name', "");
      this.set('surname', "");
      this.set('email', '');
      this.set('password', '');
      this.set('emailConfirmation', '');
      this.set('passwordConfirmation', '');
    },

    toggleUserEdit: function() {

      let user = this.get('currentUser');
      let address = this.get('currentUser.address');

      this.set('userCreateEnabled', false);
      this.set('userEditEnabled', true);

      this.set('name', user.name);
      this.set('surname', user.surname);
      this.set('dateOfBirth', user.dateOfBirth);
      this.set('phoneNumber', user.phoneNumber);
      this.set('selectedOption', user.gender);
      this.set('street', address.get('street'));
      this.set('city', address.get('city'));
      this.set('zipCode', address.get('zipCode'));
      this.set('state', address.get('state'));
      this.set('country', address.get('country'));
     
      this.set('selectedOption', null);
      this.set('nameHasError', null);
      this.set('nameErrorMessage', null);
      this.set('surnameHasError', null);
      this.set('surnameErrorMessage', null);
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
    },

    cancle: function() {
      this.set('userEditEnabled', false);
      this.set('userCreateEnabled', false);
    },

    setSelection: function(selected) {
      this.set('selectedOption', selected)
    },

    clearFields: function(){
      this.set('currentUser', null);
      this.set('userCreateEnabled', false);
      this.set('userEditEnabled', false);
      this.set('selectedOption', null);
      this.set('userCreateEnabled', false);
      this.set('userEditEnabled', false);
      this.set('selectedOption', null);
      this.set('nameHasError', null);
      this.set('nameErrorMessage', null);
      this.set('surnameHasError', null);
      this.set('surnameErrorMessage', null);
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
      this.set('countryErrorMessage', null);
      this.set('name', "");
      this.set('surname', "");
      this.set('email', '');
      this.set('password', '');
      this.set('emailConfirmation', '');
      this.set('passwordConfirmation', '');
      this.set('dateOfBirth', "");
      this.set('phoneNumber', "");
      this.set('selectedOption', "");
      this.set('street', "");
      this.set('city', "");
      this.set('zipCode', "");
      this.set('state', "");
      this.set('country', "");
    }

  }
});

import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { inject as service } from '@ember/service';
import swal from 'sweetalert';

export default Controller.extend({
  store: service(),
  currentDate: moment(new Date()).format("DD/MM/YYYY"),

  currentUser: null,
  userCreateEnabled: false,
  userEditEnabled: false,
  
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
    },

    toggleUserEdit: function() {
      this.set('userCreateEnabled', false);
      this.set('userEditEnabled', true);
    },

    createUser: function() {
      var _this = this;

      let user = this.store.createRecord('user');
      // category.set('name', this.get('parentNameInput'));
      // category.set('parent_id', null);
      // category.save().then(function(){
      //   _this.set('parentNameInput', '');
      //   _this.set('categoryInputEnabled', false);
      // });
    },

    updateUser: function(){

    },


    deleteUser: function(user) {
      user.destroyRecord();
    },

    clearFields: function(){
      this.set('currentUser', null);
      this.set('userCreateEnabled', false);
      this.set('userEditEnabled', false);
      // this.set('categoryNameHasError', null);
      // this.set('categoryNameErrorMessage', null);
      // this.set('subCategoryNameHasError', null);
      // this.set('subCategoryNameErrorMessage', null);
    }

  }
});

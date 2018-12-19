import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import swal from 'sweetalert';

export default Controller.extend({

  currentDate: moment(new Date()).format("DD/MM/YYYY"),

  productNameHasError: null,
  productNamePriceErrorMessage: null,

  descriptionHasError: null,
  descriptionErrorMessage: null,

  categoryHasError: null,
  categoryErrorMessage: null,

  subCategoryHasError: null,
  subCategoryErrorMessage: null,

  startingPriceHasError: null,
  startingPriceErrorMessage: null,

  publishDateHasError: null,
  publishDateErrorMessage: null,

  expiryDateHasError: null,
  expiryDateErrorMessage: null,

  phoneNumberHasError: null,
  phoneNumberErrorMessage: null,

  streetHasError: null,
  streetErrorMessage: null,

  cityHasError: null,
  cityErrorMessage: null,

  zipCodeHasError: null,
  zipCodeErrorMessage: null,

  countryHasError: null,
  countryErrorMessage: null,

  page: 1,
  category: null,
  subCategory: null,
  name: null,
  description: null,
  startingPrice: null,
  startDate: null,
  endDate: null,
  address: null,
  city: null,
  country: null,
  zipCode: null,
  phone: null,

  customValidationPageOne: function(){
    var checker = true;
 
    if(isEmpty(this.get('nameInput')) || (this.get('nameInput').length != 0 && this.get('nameInput').trim().length == 0)) {
      this.set('productNameHasError', true);
      this.set('productNameErrorMessage', 'Product name can not be blank');
      checker = false;
    } else {
      if(this.get('nameInput').length > 60){
        this.set('productNameHasError', true);
        this.set('productNameErrorMessage', 'The given name is way too big');
        checker = false;
      } else {
        this.set('productNameHasError', false);
      }
    }

    if(!(isEmpty(this.get('category')))) {
      this.set('categoryHasError', false);
    } else {
      this.set('categoryHasError', true);
      this.set('categoryErrorMessage', 'Category can not be blank');
      checker = false;
    }

    if(!(isEmpty(this.get('subCategory')))) {
      this.set('subCategoryHasError', false);
    } else {
      this.set('subCategoryHasError', true);
      this.set('subCategoryErrorMessage', 'SubCategory can not be blank');
      checker = false;
    }

    if(isEmpty(this.get('descriptionInput')) || (this.get('descriptionInput').length != 0 && this.get('descriptionInput').trim().length == 0)) {
      this.set('descriptionHasError', true);
      this.set('descriptionErrorMessage', 'Description can not be blank');
      checker = false;
    } else {
      if(this.get('descriptionInput').length > 700){
        this.set('descriptionHasError', true);
        this.set('descriptionErrorMessage', 'The given description is way too big');
        checker = false;
      } else {
        this.set('descriptionHasError', false);
      }
    }

    return checker;
  },

  customValidationPageTwo: function(){
    var checker = true;
    var regex;

    if(isEmpty(this.get('startingPriceInput')) || (this.get('startingPriceInput').length != 0 && this.get('startingPriceInput').trim().length == 0)) {
      this.set('startingPriceHasError', true);
      this.set('startingPriceErrorMessage', 'Product name can not be blank');
      checker = false;
    } else {
      regex = new RegExp(/^-?\d*(\.\d+)?$/);
      if(regex.test(this.get('startingPriceInput'))){
        if(this.get('startingPriceInput') >= 0) {
          this.set('startingPriceHasError', false);
        } else {
          this.set('startingPriceHasError', true);
          this.set('startingPriceErrorMessage', 'Please put in a number higher or equal to 0');
          checker = false;
        }
      } else {
        this.set('startingPriceHasError', true);
        this.set('startingPriceErrorMessage', 'Please put in a valid number');
        checker = false;
      }
    }

    if(isEmpty(this.get('startDateInput'))) {
      this.set('publishDateHasError', true);
      this.set('publishDateErrorMessage', 'Start Date can not be blank');
      checker = false;
    } else {
      this.set('publishDateHasError', false);
    }

    if(isEmpty(this.get('endDateInput')) && isEmpty(this.get('endDate'))) {
      this.set('expiryDateHasError', true);
      this.set('expiryDateErrorMessage', 'End Date can not be blank');
      checker = false;
    } else {
      this.set('expiryDateHasError', false);
    }

    return checker;
  },

  customValidationPageThree: function(){
    var checker = true;
    var regex = new RegExp(/^\+{0,1}[0-9, ]*$/);
    
    if(isEmpty(this.get('addressInput')) || (this.get('addressInput').length != 0 && this.get('addressInput').trim().length == 0)) {
      this.set('addressHasError', true);
      this.set('addressErrorMessage', 'Address can not be blank');
      checker = false;
    } else {  
      this.set('addressHasError', false);
    }

    if(isEmpty(this.get('cityInput')) || (this.get('cityInput').length != 0 && this.get('cityInput').trim().length == 0)) {
      this.set('cityHasError', true);
      this.set('cityErrorMessage', 'City can not be blank');
      checker = false;
    } else {
      this.set('cityHasError', false);  
    }

    if(isEmpty(this.get('countryInput')) || (this.get('countryInput').length != 0 && this.get('countryInput').trim().length == 0)) {
      this.set('countryHasError', true);
      this.set('countryErrorMessage', 'Country can not be blank');
      checker = false;
    } else {
      this.set('countryHasError', false);  
    }

    if(isEmpty(this.get('zipCodeInput')) || (this.get('zipCodeInput').length != 0 && this.get('zipCodeInput').trim().length == 0)) {
      this.set('zipCodeHasError', true);
      this.set('zipCodeErrorMessage', 'ZipCode can not be blank');
      checker = false;
    } else {
      this.set('zipCodeHasError', false);  
    }

    if(isEmpty(this.get('phoneInput')) || (this.get('phoneInput').length != 0 && this.get('phoneInput').trim().length == 0)) {
      this.set('phoneHasError', true);
      this.set('phoneErrorMessage', 'Phone name can not be blank');
      checker = false;
    } else {
      if(regex.test(this.get('phoneInput'))){  
        this.set('phoneHasError', false);
      } else {
        this.set('phoneHasError', true);
        this.set('phoneErrorMessage', 'Please put in a valid phone number');
        checker = false;
      }
    }


    return checker;
  },


  actions : {

    create: function(){
      if(this.customValidationPageThree()){
        var _this = this;

        let product = this.store.createRecord('product');
        product.set('name', this.get('name'));
        product.set('description', this.get('description'));
        product.set('publishDate', this.get('startDate'));
        product.set('expireDate', this.get('endDate'));
        product.set('startingPrice', this.get('startingPrice'));
        product.set('category_id', this.get('subCategory'));
        product.set('status', 'Active');

        let sale = this.store.createRecord('sale');
        sale.set('address', this.get('addressInput'));
        sale.set('city', this.get('cityInput'));
        sale.set('zipCode', this.get('zipCodeInput'));
        sale.set('country', this.get('countryInput'));
        sale.set('phone', this.get('phoneInput'));
        sale.set('status', 'Active');
        sale.set('product', product);

        sale.save().then(function(){
          _this.transitionToRoute('account.sell.entry');
          swal("Sale Succesfully Posted!", "You have successfully posted your product!", "success");
        }).catch(function(){
          swal("Ooops!", "It would seem an error has occurred please try again.", "error");
        });
      }
    },

    setCategory: function(category){
      this.set('category', category);
    },

    setSubCategory: function(category){
      this.set('subCategory', category);
    },
    
    changeStartDate: function(date){
      this.set('startDate', date);
      this.set('endDateInput', '');
      this.set('endDate', null);
    },

    changeEndDate: function(date){
      this.set('endDate', date);
    },

    setPage: function(page){
      var currentPage = this.get('page');
      if(page > currentPage) {
        if(page == 2) {
          if(this.customValidationPageOne()){
            this.set('name', this.get('nameInput'));
            this.set('description', this.get('descriptionInput'));
            this.set('page', page);
          }
        }
        
        if(page == 3) {
          if(this.customValidationPageTwo()){
            this.set('startingPrice', this.get('startingPriceInput'));
            this.set('startDate', this.get('startDateInput'));
            this.set('endDate', this.get('endDateInput'));
            this.set('page', page);
          }
        }        
      } else {
        if(page == 2) {
          this.set('address', this.get('addressInput'));
          this.set('city', this.get('cityInput'));
          this.set('country', this.get('countryInput'));
          this.set('zipCode', this.get('zipCodeInput'));
          this.set('phone', this.get('phoneInput'));
          this.set('page', page);
        } else {
          this.set('page', page);
        }
      }
    },

    clearFields: function(){
      this.set('page', 1);
      this.set('category', null);
      this.set('subCategory', null);
      this.set('name', null);
      this.set('description', null);
      this.set('startingPrice', null);
      this.set('startDate', null);
      this.set('endDate', null);
      this.set('address', null);
      this.set('city', null);
      this.set('country', null);
      this.set('zipCode', null);
      this.set('phone', null);
      this.set('nameInput', null);
      this.set('descriptionInput', null);
      this.set('startingPriceInput', null);
      this.set('publishDate', null);
      this.set('expiryDate', null);
      this.set('addressInput', null);
      this.set('cityInput', null);
      this.set('countryInput', null);
      this.set('zipCodeInput', null);
      this.set('phoneInput', null);
    }

  }

});

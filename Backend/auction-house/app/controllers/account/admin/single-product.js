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

  page: 1,
  category: null,
  subCategory: null,
  name: null,
  description: null,
  startingPrice: null,
  startDate: null,
  endDate: null,

  customValidation: function(){
    var checker = true;
    var regex;
    
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


  actions : {

    saveProduct: function(){
      if(this.customValidation()){
        var _this = this;

        let product = this.get('model.product');
        product.set('name', this.get('nameInput'));
        product.set('description', this.get('descriptionInput'));
        product.set('publishDate', this.get('startDateInput'));
        product.set('expireDate', this.get('endDateInput'));
        product.set('startingPrice', this.get('startingPriceInput'));
        product.set('category_id', this.get('subCategory'));
        product.set('status', 'Active');

        product.save().then(function(){
          swal("Success!", "You have successfully updated this product!", "success");
        }).catch(function(){
          swal("Ooops!", "It would seem an error has occurred please try again.", "error");
        });
      }
    },

    deleteProduct: function(){ 
      var _this = this;
      let product = this.get('model.product');

      product.destroyRecord().then(function(){
        _this.transitionToRoute('account.admin.products');
        swal("Success!", "You have successfully deleted this product!", "success");
      }).catch(function(){
        swal("Ooops!", "It would seem an error has occurred please try again.", "error");
      });
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

    clearFields: function(){
      this.set('page', 1);
      this.set('category', null);
      this.set('subCategory', null);
      this.set('name', null);
      this.set('description', null);
      this.set('startingPrice', null);
      this.set('startDate', null);
      this.set('endDate', null);
      this.set('nameInput', null);
      this.set('descriptionInput', null);
      this.set('startingPriceInput', null);
      this.set('publishDate', null);
      this.set('expiryDate', null);

      this.set('productNameHasError', null);
      this.set('productNameErrorMessage', null);
      this.set('descriptionHasError', null);
      this.set('descriptionErrorMessage', null);
      this.set('categoryHasError', null);
      this.set('categoryErrorMessage', null);
      this.set('subCategoryHasError', null);
      this.set('subCategoryErrorMessage', null);
      this.set('startingPriceHasError', null);
      this.set('startingPriceErrorMessage', null);
      this.set('publishDateHasError', null);
      this.set('publishDateErrorMessage', null);
      this.set('expiryDateHasError', null);
      this.set('expiryDateErrorMessage', null);
    }
  }
});
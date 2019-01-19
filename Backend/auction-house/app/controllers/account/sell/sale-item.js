import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import EmberObject, { computed, observer } from '@ember/object';
import { inject as service } from '@ember/service';
import $ from 'jquery';
import S3Uploader from 'ember-uploader/uploaders/s3';
import ENV from 'auction-house/config/environment';
import swal from 'sweetalert';
import RSVP from 'rsvp';

export default Controller.extend({
  signingUrl: ENV.HOST_URL+'/api/v1/validate/product/image',
  loadingSlider: service(),
  customSession: service(),

  currentDate: moment(new Date()).format("DD/MM/YYYY"),

  productNameHasError: null,
  productNamePriceErrorMessage: null,

  descriptionHasError: null,
  descriptionErrorMessage: null,

  picturesHaveError: null,
  picturesErrorMessage: null,
  
  categoryHasError: null,
  categoryErrorMessage: null,

  subCategoryHasError: null,
  subCategoryErrorMessage: null,

  colorHasError: null,
  colorErrorMessage: null,

  sizeHasError: null,
  sizeErrorMessage: null,

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
  color: null,
  size: null,
  startingPrice: null,
  startDate: null,
  endDate: null,
  address: null,
  city: null,
  country: null,
  zipCode: null,
  phone: null,

  pictureFiles: [],

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
      if(this.get('descriptionInput').length > 700) {
        this.set('descriptionHasError', true);
        this.set('descriptionErrorMessage', 'The given description is way too big');
        checker = false;
      } else {
        this.set('descriptionHasError', false);
      }
    }

    if(isEmpty(this.get('colorInput')) || (this.get('colorInput').length != 0 && this.get('colorInput').trim().length == 0)) {
      this.set('colorHasError', true);
      this.set('colorErrorMessage', 'Color can not be blank');
      checker = false;
    } else {
      if(this.get('colorInput').length > 40){
        this.set('colorHasError', true);
        this.set('colorErrorMessage', 'The given color is way too big');
        checker = false;
      } else {
        this.set('colorHasError', false);
      }
    }

    if(isEmpty(this.get('sizeInput')) || (this.get('sizeInput').length != 0 && this.get('sizeInput').trim().length == 0)) {
      this.set('sizeHasError', true);
      this.set('sizeErrorMessage', 'Size can not be blank');
      checker = false;
    } else {
      if(this.get('sizeInput').length > 40){
        this.set('sizeHasError', true);
        this.set('sizeErrorMessage', 'The given size is way too big');
        checker = false;
      } else {
        this.set('sizeHasError', false);
      }
    }

    if((this.get('pictureFiles').length + this.get('model.saleItem.product.pictures').length) >= 3 
      && (this.get('pictureFiles').length + this.get('model.saleItem.product.pictures').length) <= 5) {
      this.set('picturesHaveError', false);
    } else {
      if((this.get('pictureFiles').length + this.get('model.saleItem.product.pictures').length) >= 3) {
        this.set('picturesHaveError', true);
        this.set('picturesErrorMessage', 'Too many images, max amount is 5 images');
        checker = false;
      } else {
        this.set('picturesHaveError', true);
        this.set('picturesErrorMessage', 'You need to add atleast 3 images');
        checker = false;
      }
    }

    return checker;
  },

  customValidationPageTwo: function(){
    var checker = true;
    var regex;

    if(isEmpty(this.get('startingPriceInput'))) {
      this.set('startingPriceHasError', true);
      this.set('startingPriceErrorMessage', 'Product price can not be blank');
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
      this.set('phoneErrorMessage', 'Phone can not be blank');
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

  addedFileEvent: computed(function() {
    var _this = this;
    return function(file) {
      var myDropzone = Dropzone.forElement("#dropzone-id");
      myDropzone.removeAllFiles(true);

      _this.get('pictureFiles').addObject(file);
    };
  }),

  previewImages: observer('page', 'pictureFiles.[]', function(){ 
    this.get('pictureFiles').forEach((item, index) => {
      var reader = new FileReader();

      reader.onload = function (e) {
        $('#preview-image-' + index).attr('src', e.target.result);
      }

      reader.readAsDataURL(item);
    });
  }),


  actions : {

    async create(){
      if(this.customValidationPageThree()){
        var _this = this;
        _this.get('loadingSlider').startLoading();

        let sale = this.get('model.saleItem');
        let product = sale.get('product');
        product.set('name', this.get('name'));
        product.set('description', this.get('description'));
        
        if(!isEmpty(this.get('startDate'))){
          var startDate = new Date(this.get('startDateInput'));
          startDate.setMinutes(startDate.getMinutes() - startDate.getTimezoneOffset());
          product.set('publishDate', startDate);  
        } 

        if(!isEmpty(this.get('endDate'))){
          var endDate = new Date(this.get('endDateInput'));
          endDate.setMinutes(endDate.getMinutes() - endDate.getTimezoneOffset());
          product.set('expireDate', endDate);  
        } 

        product.set('color', this.get('color'));
        product.set('size', this.get('size'));
        product.set('startingPrice', this.get('startingPrice'));
        product.set('category_id', this.get('subCategory'));
        product.set('status', 'Active');

        sale.set('address', this.get('addressInput'));
        sale.set('city', this.get('cityInput'));
        sale.set('zipCode', this.get('zipCodeInput'));
        sale.set('country', this.get('countryInput'));
        sale.set('phone', this.get('phoneInput'));
        sale.set('status', 'Active');
        sale.set('product', product);
        
        let promises = [];
  
        this.get('pictureFiles').forEach((file, index) => {
          var promise = new RSVP.Promise(function(resolve, reject){
            const uploader = S3Uploader.create({
              signingUrl: _this.get('signingUrl'),
              signingAjaxSettings: {
                headers: {
                  'X-AUTH-TOKEN': _this.get('customSession').getAuthToken()
                }
              }
            });

            uploader.on('didUpload', response => {
              let uploadedUrl = $(response).find('Location')[0].textContent;
              uploadedUrl = decodeURIComponent(uploadedUrl);

              let picture = _this.store.createRecord('picture');
              picture.set('url', uploadedUrl);
              product.get('pictures').pushObject(picture);

              resolve();
            });

            uploader.upload(file);
          });

          promises.push(promise)
        });

        await RSVP.all(promises).then(function(){
          sale.save().then(function(){
            _this.store.unloadAll('sale');
            _this.get('loadingSlider').endLoading();
            _this.transitionToRoute('account.sales');
            swal("Sale Succesfully Updated!", "You have successfully updated your product!", "success");
          }).catch(function(){
            _this.get('loadingSlider').endLoading();
            swal("Ooops!", "It would seem an error has occurred please try again.", "error");
          });
        });
       
      }
    },

    async delete(){
       var _this = this;
        _this.get('loadingSlider').startLoading();

        let sale = this.get('model.saleItem');
        await sale.destroyRecord().then(function(){
          _this.get('loadingSlider').endLoading();
          _this.transitionToRoute('account.sales');
          swal("Sale Succesfully Deleted!", "You have successfully deleted your product!", "success");
        }).catch(function(){
          _this.get('loadingSlider').endLoading();
          swal("Ooops!", "It would seem an error has occurred please try again.", "error");
        });
    },

    removePicture: function(picture){
      this.get('pictureFiles').removeObject(picture);
    },

    removeOldPicture: function(picture){
      this.get('model.saleItem.product.pictures').removeObject(picture);
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
            this.set('color', this.get('colorInput'));
            this.set('size', this.get('sizeInput'));
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
      this.store.unloadAll('sale');
      this.set('page', 1);
      this.set('category', null);
      this.set('subCategory', null);
      this.set('name', null);
      this.set('description', null);
      this.set('color', null);
      this.set('size', null);
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
      this.set('colorInput', null);
      this.set('sizeInput', null);
      this.set('startingPriceInput', null);
      this.set('publishDate', null);
      this.set('expiryDate', null);
      this.set('addressInput', null);
      this.set('cityInput', null);
      this.set('countryInput', null);
      this.set('zipCodeInput', null);
      this.set('phoneInput', null);
      this.set('pictureFiles', []);


      this.set('productNameHasError', null);
      this.set('productNameErrorMessage', null);
      this.set('descriptionHasError', null);
      this.set('descriptionErrorMessage', null);
      this.set('picturesHaveError', null);
      this.set('picturesErrorMessage', null);
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
      this.set('addressHasError', null);
      this.set('addressErrorMessage', null);
      this.set('cityHasError', null);
      this.set('cityErrorMessage', null);
      this.set('countryHasError', null);
      this.set('countryErrorMessage', null);
      this.set('zipCodeHasError', null);
      this.set('zipCodeErrorMessage', null);
      this.set('phoneNumberHasError', null);
      this.set('phoneNumberErrorMessage', null);
    }

  }

});

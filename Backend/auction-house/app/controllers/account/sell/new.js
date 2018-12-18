import Controller from '@ember/controller';

export default Controller.extend({

  productNameHasError: null,
  productNamePriceErrorMessage: null,

  descriptionHasError: null,
  descriptionErrorMessage: null,

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

  actions : {
    createSale: function(){

    },

    setCategory: function(category){
      this.set('category', category);
      console.log(this.get('description'));
    },

    setSubCategory: function(category){
      this.set('subCategory', category);
    },

    setPage: function(page){
      this.set('page', page);
    }

  }

});

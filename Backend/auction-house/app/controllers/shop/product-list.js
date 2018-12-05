import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';

export default Controller.extend({
  queryParams: ['name', 'parentCategory', 'childCategory'],
  name: null,
  displayDetails: false,
  parentCategory: null,
  childCategory: null,
  selectedColor: null,
  selectedSize: null,
  selectedListType: null,

  filteredProducts: Ember.computed('name', 'parentCategory', 'childCategory', 'selectedColor', 'selectedSize', function(){
 
    var selI = this.get('childCategory');
    var products = this.get('model.productList');
    var _this = this;
    let singleProduct = null;

    if(this.get('parentCategory') !== null && this.get('parentCategory') !== 0) {
      products = products.filter(
        function(product) { 
          let categories = product.get('productcategory');
          categories.forEach(function(item){
            if((item.get('category').get('parent_id') == _this.get('parentCategory'))){
              singleProduct = product;
            } else {
              singleProduct = null;
            }

          });
          return singleProduct;
        }
      );
    }

    if(this.get('childCategory') !== null && this.get('childCategory') !== 0) {
      products = products.filter(
        function(product) { 
          let categories = product.get('productcategory');
          categories.forEach(function(item){
            if(item.get('category').get('id') == selI){
              singleProduct = product;
            } else {
              singleProduct = null;
            }

          });
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('selectedColor')))) {
      products = products.filter(
        function(product) { 
          if(product.color == _this.get('selectedColor')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('selectedSize')))) {
      products = products.filter(
        function(product) { 
          if(product.size == _this.get('selectedSize')) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    if(!(isEmpty(this.get('name')))) {
      products = products.filter(
        function(product) { 
          if(product.name.toLowerCase().includes(_this.get('name').toLowerCase())) {
            singleProduct = product;
          } else {
            singleProduct = null;
          }
          return singleProduct;
        }
      );
    }

    return products;

  }),

  actions: {
    toggleDetails: function(category) {

      if(category.id !== this.get('parentCategory')){
        this.set('parentCategory', category.id);
        this.set('selectedCategory', null);
        this.set('displayDetails', true);
      } else {
        this.set('parentCategory', null);
        this.set('childCategory', null);
        this.toggleProperty('displayDetails');
      }

    },

    setSelection: function(selected) {
      if(selected === null){
        this.set('childCategory', selected);  
        this.set('parentCategory', selected);  
      } else {
        this.set('childCategory', selected.id);  
      }
    },

    setColor: function(selected) {
      this.set('selectedColor', selected);
    },

    setSize: function(selected) {
      this.set('selectedSize', selected);  
    },

    setListType: function(selected) {
      this.set('selectedListType', selected);  
    },

    createWishlist: function(productID) {
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save();
    },

    clearFields: function(){
      this.set('displayDetails', false);
      this.set('name', null);
      this.set('parentCategory', null);
      this.set('childCategory', null);
      this.set('selectedColor', null); 
      this.set('selectedSize', null); 
      this.set('selectedListType', null); 
    }
  }
});

import Controller from '@ember/controller';
import { isEmpty } from '@ember/utils';
import { stringSimilarity } from "string-similarity-js";

export default Controller.extend({
  store: Ember.inject.service(),

  queryParams: ['selectedSorting', 'selectedListType', 'parentCategory', 'childCategory', 'selectedColor', 'selectedSize', 'name'],
  name: '',
  parentCategory: null,
  childCategory: null,
  selectedSorting: null,
  selectedColor: null,
  selectedSize: null,
  selectedListType: null,
  checker: null,
  allWishlistItems: null,
  closestName: null,
  nameNotFound: false,

  wishlist: Ember.computed(function(){
    const store = this.get('store');
    return this.store.findAll('wishlist', { reload: true });
  }).volatile(),

  wishlistItems: Ember.computed('wishlist', function(){
    this.set('allWishlistItems', this.get('wishlist'));

    return this.get('allWishlistItems');
  }).volatile(),



  filteredProducts: Ember.computed('name', 'selectedSorting', 'parentCategory', 'childCategory', 'selectedColor', 'selectedSize', function(){
 
    var products = this.get('model.productList');
    var _this = this;
    let singleProduct = null;

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

      if(!(isEmpty(products))){
        _this.set('nameNotFound', false);
      }
    }

    if(isEmpty(this.get('name'))){
      _this.set('nameNotFound', false);
    }

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
            if(item.get('category').get('id') == _this.get('childCategory')){
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

    if(this.get('selectedSorting') == 'popularity'){
      return products.sortBy('bids.length').reverse();
    }

    if(this.get('selectedSorting') == 'rating-high'){
      return products.sortBy('averageScore').reverse();
    }

    if(this.get('selectedSorting') == 'rating-low'){
      return products.sortBy('averageScore');
    }

    if(this.get('selectedSorting') == 'newest'){
      return products.sortBy('publishDate');
    }

    if(this.get('selectedSorting') == 'oldest'){
      return products.sortBy('expireDate');
    }

    if(this.get('selectedSorting') == 'price-low'){
      return products.sortBy('startingPrice');
    }

    if(this.get('selectedSorting') == 'price-high'){
      return products.sortBy('startingPrice').reverse();
    }

    return products;

  }),

  productNameErrorMessage: Ember.computed('filteredProducts', function(){

    var nameChecker = null;
    var similarityRate = 0;
    var _this = this;

    if(isEmpty(this.get('filteredProducts'))){
      var products = this.get('model.productList');
      _this.set('closestName', null);
      products.forEach((item, index) => {
        if(stringSimilarity(item.name, this.get('name')) > similarityRate){
          similarityRate = stringSimilarity(item.name, this.get('name'));
          nameChecker = item.name;
          _this.set('closestName', nameChecker);
        }
      });

      _this.set('nameNotFound', true);
    
      }
      return _this.get('nameNotFound');
  }),

  actions: {
    toggleDetails: function(category) {

      if(category.id !== this.get('parentCategory')){
        this.set('parentCategory', category.id);
      } else {
        this.set('parentCategory', null);
        this.set('childCategory', null);
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

    setSortType: function(selected){
      this.set('selectedSorting', selected);  
    },

    createWishlist: function(productID) {
      var _this = this;
      // console.log(this.get('allWishlistItems'));
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save().then(function(data){
        // console.log(_this.get('allWishlistItems'));
        // _this.get('allwishlistItems').addObject(data);
        // console.log(_this.get('allwishlistItems'));
      });
    },

    deleteWishlist: function(wishlistItem) {
      var _this = this;
      var currentWishlistItem = wishlistItem;
      currentWishlistItem.destroyRecord().then(function(){
        _this.get('allWishlistItems').removeObject(wishlistItem);
      });
    },

    clearFields: function(){
      this.set('name', null);
      this.set('parentCategory', null);
      this.set('childCategory', null);
      this.set('selectedSorting', null)
      this.set('selectedColor', null); 
      this.set('selectedSize', null); 
      this.set('selectedListType', null); 
      this.set('closestName', null);
      this.set('nameNotFound', false);
    }
  }
});

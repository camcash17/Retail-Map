import React, { Component } from 'react';
import './App.css';
import axios from 'axios';
import Infinite from 'react-infinite';
import Marker from './Marker';
import AddFavorites from './AddFavorites';

class App extends Component {
  constructor(){
    super();
    this.state = {
      selected: "",
      selectedList: false,
    }
  }

  //method that makes a call to the database and stores the resukts in state
  componentDidMount() {
    axios.get(`/retailers`)
    .then(function (response) {
      console.log('response', response.data)
      this.setState({
        retailers: response.data,
      })
    }.bind(this))
    .catch(function (error) {
      console.log("Error retrieving retail data");
      console.log(error);
    })
  }

  //method that queries the external API call to retrieve data to set as state. sets a boolean value false to clear data
  handleSecondaryChange = async (event) => {
    this.setState({
      results: null
    })
    axios.get(`https://data.cityofnewyork.us/resource/uyz2-yxi9.json?$q=${event.target.value}`)
    .then(function (response) {
      this.setState({
        results: response.data,
        selectedList: false
      })
    }.bind(this))
    .catch(function (error) {
      console.log("Error Setting Selected State");
      console.log(error);
    })
  }

  //method to to take results of the secondary category chosen and define a variable to set as state and save data necessary to add to favorites
  handlePrimaryChange = async (event) => {
    let secondaryList;
    secondaryList = this.state.results.filter((secondary, index) => {
      if(event.target.value === secondary.secondary && secondary.latitude && secondary.longitude) {
        return (
          secondary.cnbio_org_name, 
          secondary.primary,
          secondary.secondary,
          secondary.latitude,
          secondary.longitude
        )
      }
    })
    this.setState({
      selectedList: true,
      secondaryList: secondaryList
    })
  }

  //method to delete a favorite item and remove it from the DB via axios call from the array that lives in state
  deleteFavorite = async (retailerId, index) => {
    try {
      await axios.delete(`/retailers/${retailerId}`);
      const updatedRetailersList = [...this.state.retailers];
      updatedRetailersList.splice(index, 1);
      this.setState({ retailers: updatedRetailersList });
    } catch (error) {
      console.log(`Error deleting Retailer with ID of ${retailerId}`);
      console.log(error);
    }
  };

  //method to add favorite and add it to the DB via axios call and push the new value to the array that lives in state
  addFavorite = async (retailer, index) => {
    console.log('create', retailer)
    try {
        // this.state.retailers.map((favorite, index) => {
        //   if(retailer.lat === favorite.lat)
        //     return(
              
        //     )
        // })
        const newFavoriteResponse = await axios.post(`/retailers`, retailer);

        const updatedFavoriteList = [...this.state.retailers];
        updatedFavoriteList.push(newFavoriteResponse.data);
        this.setState({ 
            retailers: updatedFavoriteList,
          });
    } catch (error) {
    console.log("Error creating new Favorite!");
    console.log(error);
    }
  };

  render() {
    //conditional statements to filter secondary category names from external API data and then remove duplicates
    let secondaries;
    let secondariesList;
    if(this.state.results) {
        secondaries = this.state.results.map((secondary, i) => {
          return (
            secondary.secondary
          )
        })
        secondariesList = secondaries.filter((val, id, array) => {
          return array.indexOf(val) === id;  
        })
    }

    return (
      <div className="App">
        <h1>Lower Manhattan Retail Map</h1>
          <div>
              <select name="primary-list" 
              onChange={(event) => this.handleSecondaryChange(event)}
              >
                <option>Select a Retail Type</option>
                <option value="Casual">Casual Eating & Takeout</option>
                <option value="Personal">Personal and Professional Services</option>
                <option value="Dining">Full Service Dining</option>
                <option value="Shopping">Shopping</option>
                <option value="Community">Community Resources</option>
                <option value="Nightlife">Nightlife</option>
                <option value="Visitor">Visitor Services</option>
            </select>
          </div>
          <br />
          <hr />
          <div className="layout">
            <div>
              <h1>Retail Locations</h1>
              {this.state.results ?
              <div>
                <select name="secondary-list" 
                  onChange={(event) => this.handlePrimaryChange(event)}
                  >
                    <option>Select a Category</option>
                    {secondariesList.map((secondary, i) => {
                      return (
                        <option key={i} value={secondary} style={{margin: '10px', padding: '5px'}}>{secondary}</option>
                      )
                    })}
                </select>
              </div>
              : null}
              {this.state.selectedList ?
              <Infinite containerHeight={500} elementHeight={40}>
                <div>
                  {this.state.secondaryList.map((selected, i) => {
                    return (
                      selected ? 
                      <div key={i} >
                        <h4>{selected.cnbio_org_name}</h4>
                        <p>{selected.cnadrprf_addrline1}</p>
                        <AddFavorites  
                          selected={selected}
                          retailers={this.state.retailers}
                          addFavorite={this.addFavorite}
                          />
                      </div> 
                      : null
                    )
                  })} 
                </div>
              </Infinite>
              : null}
             </div>
             {this.state.retailers ?
            <Marker selected={this.state.secondaryList} favorites={this.state.retailers} selectedList={this.state.selectedList} /> 
            : null }
            <div>
              <h1>Favorites</h1>
              {this.state.retailers ?
              <Infinite containerHeight={500} elementHeight={40}>
                <div>
                  {this.state.retailers.map((favorites, index) => {
                    return (
                      <div key={index}>
                        <p key={favorites.id} style={{margin: '10px', padding: '5px'}}>{favorites.orgName}</p>
                        <button
                          onClick={() => this.deleteFavorite(favorites.id, index)}>
                          Delete
                        </button>
                      </div>
                    )
                  })}
                </div>
              </Infinite>
              : null }
            </div>
          </div>
      </div>
    ) 
  }
}

export default App;

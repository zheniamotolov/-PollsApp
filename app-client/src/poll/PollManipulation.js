import React, {Component} from 'react';
import SearchPoll from './SearchPoll'
import {withRouter} from 'react-router-dom';
import PollList from "./PollList";
class PollManipulation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            query: '',
            polls: {},
            totalElements:{},
            totalPages:{},
            loading: false,
            message: '',
        }

        this.handleOnInputChange = this.handleOnInputChange.bind(this);
        this.searchApts = this.searchApts.bind(this);
    }


    handleOnInputChange = (e) => {
        const query = e.target.value;
        if (!query) {
            this.setState({query, results: {},  message: ''})
        } else {
            this.setState({query, loading: true, message: ''}, () => {
                this.fetchSearchResults(0, query) // important moment that we call fetchSearchResults inside
                // callback function of setState, thaw because setState is asynchronus we sure that fetchSearchResults would
                // be called after setState
            })
        }
    }

    render() {
        return (
            <div>
                <SearchPoll handleOnInputChange={this.handleOnInputChange}/>

            </div>
        )
    }
}

export default withRouter(PollManipulation)
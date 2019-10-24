import React, {Component} from 'react'
import {Input} from "antd";



class SearchPoll extends Component {
    render() {
        return (
            <div  style={{marginBottom:25}}>
                <Input
                    placeholder="Search..."
                    // enterButton="Search"
                    // size="large"
                    onChange={e => this.props.handleSearchChange(e.target.value)}
                >
                </Input>
            </div>
        )
    }
}

export default SearchPoll;
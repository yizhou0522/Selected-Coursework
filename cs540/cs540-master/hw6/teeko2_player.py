# Yizhou Liu
# liu773@wisc.edu
# cs540 
import random

class Teeko2Player:
    """ An object representation for an AI game player for the game Teeko2.
    """
    board = [[' ' for j in range(5)] for i in range(5)]
    pieces = ['b', 'r']

    
    def __init__(self):
        """ Initializes a Teeko2Player object by randomly selecting red or black as its
        piece color.
        """
        self.my_piece = random.choice(self.pieces)
        self.opp = self.pieces[0] if self.my_piece == self.pieces[1] else self.pieces[1]
        self.table_points = self.init_table()

    def make_move(self, state):
        """ Selects a (row, col) space for the next move. You may assume that whenever
        this function is called, it is this player's turn to move.

        Args:
            state (list of lists): should be the current state of the game as saved in
                this Teeko2Player object. Note that this is NOT assumed to be a copy of
                the game state and should NOT be modified within this method (use
                place_piece() instead). Any modifications (e.g. to generate successors)
                should be done on a deep copy of the state.

                In the "drop phase", the state will contain less than 8 elements which
                are not ' ' (a single space character).

        Return:
            move (list): a list of move tuples such that its format is
                    [(row, col), (source_row, source_col)]
                where the (row, col) tuple is the location to place a piece and the
                optional (source_row, source_col) tuple contains the location of the
                piece the AI plans to relocate (for moves after the drop phase). In
                the drop phase, this list should contain ONLY THE FIRST tuple.

        Note that without drop phase behavior, the AI will just keep placing new markers
            and will eventually take over the board. This is not a valid strategy and
            will earn you no points.
        """

        drop_phase = True  
        # TODO: detect drop phase
        pieces = 0
        for i in state:
              for j in i:
                if j != ' ':
                    pieces += 1
        if pieces == 8:
            drop_phase=False
        else:
            drop_phase=True
        if not drop_phase:
            pass

        # select an unoccupied space randomly
        # TODO: implement a minimax algorithm to play better
        move = []
        possible_moves = self.succ(state, self.my_piece, self.is_drop_phase(state))
        alpha = float('-inf')
        beta = float('inf')
        max_state = ''
        for possible in possible_moves:
            tmp = self.Min_Value(possible[0], 1, alpha, beta)
            if tmp > alpha:
                alpha = tmp
                max_state =possible

        # random processing giving at first########################################
        # (row, col) = (random.randint(0,4), random.randint(0,4))
        # while not state[row][col] == ' ':
        #     (row, col) = (random.randint(0,4), random.randint(0,4))

        # ensure the destination (row,col) tuple is at the beginning of the move list
        # print(self.heuristic_game_value(m[0]))
        move.append(max_state[1])
        if max_state[2] != 0:
            move.append(max_state[2])
        return move

    def opponent_move(self, move):
        """ Validates the opponent's next move against the internal board representation.
        You don't need to touch this code.`

        Args:
            move (list): a list of move tuples such that its format is
                    [(row, col), (source_row, source_col)]
                where the (row, col) tuple is the location to place a piece and the
                optional (source_row, source_col) tuple contains the location of the
                piece the AI plans to relocate (for moves after the drop phase). In
                the drop phase, this list should contain ONLY THE FIRST tuple.
        """
        # validate input
        if len(move) > 1:
            source_row = move[1][0]
            source_col = move[1][1]
            if source_row != None and self.board[source_row][source_col] != self.opp:
                self.print_board()
                print(move)
                raise Exception("You don't have a piece there!")
            if abs(source_row - move[0][0]) > 1 or abs(source_col - move[0][1]) > 1:
                self.print_board()
                print(move)
                raise Exception('Illegal move: Can only move to an adjacent space')
        if self.board[move[0][0]][move[0][1]] != ' ':
            raise Exception("Illegal move detected")
        # make move
        self.place_piece(move, self.opp)

    def place_piece(self, move, piece):
        """ Modifies the board representation using the specified move and piece

        Args:
            move (list): a list of move tuples such that its format is
                    [(row, col), (source_row, source_col)]
                where the (row, col) tuple is the location to place a piece and the
                optional (source_row, source_col) tuple contains the location of the
                piece the AI plans to relocate (for moves after the drop phase). In
                the drop phase, this list should contain ONLY THE FIRST tuple.

                This argument is assumed to have been validated before this method
                is called.
            piece (str): the piece ('b' or 'r') to place on the board
        """
        if len(move) > 1:
            self.board[move[1][0]][move[1][1]] = ' '
        self.board[move[0][0]][move[0][1]] = piece

    def print_board(self):
        """ Formatted printing for the board """
        for row in range(len(self.board)):
            line = str(row)+": "
            for cell in self.board[row]:
                line += cell + " "
            print(line)
        print("   A B C D E")

    def game_value(self, state):
        """ Checks the current board status for a win condition

        Args:
        state (list of lists): either the current state of the game as saved in
            this Teeko2Player object, or a generated successor state.

        Returns:
            int: 1 if this Teeko2Player wins, -1 if the opponent wins, 0 if no winner

        TODO: complete checks for diagonal and diamond wins
        """
        # check horizontal wins
        for row in state:
            for i in range(2):
                if row[i] != ' ' and row[i] == row[i+1] == row[i+2] == row[i+3]:
                    return 1 if row[i]==self.my_piece else -1
        # check vertical wins
        for col in range(5):
            for i in range(2):
                if state[i][col] != ' ' and state[i][col] == state[i+1][col] == state[i+2][col] == state[i+3][col]:
                    return 1 if state[i][col]==self.my_piece else -1
        # TODO: check \ diagonal wins
        r = [1, 0, 0]
        c = [0, 0, 1]
        for i in range(3):
            for j in range(2):
                x = r[i] + j
                y = c[i] + j
                if j == 1 and x - y != 0: continue
                if state[x][y] != ' ' and state[x][y] == state[x + 1][y + 1] == state[x + 2][y + 2]==state[x + 3][y + 3]:
                    return 1 if state[x][y] == self.my_piece else -1
        # TODO: check / diagonal wins
        r = [3, 4, 4]
        c = [0, 0, 1]
        for i in range(3):
            for j in range(2):
                x = r[i] - j
                y = c[i] + j
                if j == 1 and x + y != 4: continue
                if state[x][y] != ' ' and state[x][y] == state[x - 1][y + 1] == state[x - 2][y + 2] == state[x - 3][y + 3]:
                    return 1 if state[x][y] == self.my_piece else -1
        # TODO: check diamond wins
        for i in range(5):
            for j in range(5):
                if i-1>=0 and j-1>=0 and i<4 and j<4 and state[i][j]==' ' and state[i-1][j]!=' 'and state[i-1][j] == state[i+1][j] == state[i][j - 1] == state[i][j+1]:
                    return 1 if state[i-1][j] == self.my_piece else -1
        return 0 # no winner yet

    #helper methods 
    # minimax algorithm  
    def Max_Value(self, state, depth, alpha, beta):
        if self.game_value(state) != 0:
            return self.game_value(state)
        # max depth reaches
        if depth == 4:
            return self.heuristic_game_value(state)

        for s in self.succ(state, self.my_piece, self.is_drop_phase(state)):
            alpha = max(alpha, self.Min_Value(s[0], depth + 1, alpha, beta))
            if alpha >= beta:
                return beta
        return alpha

    def Min_Value(self, state, depth, alpha, beta):
        if self.game_value(state)!= 0:
            return self.game_value(state)

        #  max depth reaches
        if depth == 4:
            return self.heuristic_game_value(state)

        for s in self.succ(state, self.opp, self.is_drop_phase(state)):
            beta = min(beta, self.Max_Value(s[0], depth + 1, alpha, beta))
            if alpha >= beta:
                return alpha
        return beta

    #get table's points
    def init_table(self):
        def calculate_points( color, pos):
            scores = [0, 1, 4, 9, 16, 25]
            if color == ' ':
                return 0
            elif color == self.my_piece:
                return scores[pos]
            else:
                return -scores[pos]
        points_list = {}
        pieces_list = [[' '], ['b'], ['r']]
        pieces_list = self.generate_list(pieces_list, 1)  
        for row in pieces_list:
            points = 0
            prev_cell = ' '
            pos = 0
            for cell in row:
                if cell == ' ':
                    if pos != 0:
                        points += calculate_points(prev_cell, pos)
                    pos = 0
                elif cell != prev_cell:
                    if pos != 0:
                        points += calculate_points(prev_cell, pos)
                    pos = 1
                else:
                    pos += 1
                prev_cell = cell

            points += calculate_points(prev_cell, pos)
            row_str = ''.join(row)
            points_list[row_str] = points
        return points_list
    
    # generate a list
    def generate_list(self, perm_list, depth):
        if depth == 5:
            return perm_list
        new_perm_list = []
        vals = [' ', 'b', 'r']
        for perm in perm_list:
            for i in range(3):
                mod_perm = perm[:]
                mod_perm.append(vals[i])
                new_perm_list.append(mod_perm)
        return self.generate_list(new_perm_list, depth + 1)
   
    @staticmethod
    def is_drop_phase(state):
        pieces = 0
        for i in state:
            for j in i:
                if j != ' ':
                    pieces += 1
        if pieces == 8:
            return False
        return True

    #generate successive moves
    @staticmethod
    def succ(state, color, drop_phase):
        succ_list = []
        if drop_phase:
            for i, row in enumerate(state):
                for j, cell in enumerate(row):
                    if cell == ' ':
                        new_state = [r[:] for r in state]
                        new_state[i][j] = color
                        succ_list.append((new_state, (i, j), 0))
        else:
            for i, row in enumerate(state):
                for j, cell in enumerate(row):
                    if cell == color:
                        for x in range(i - 1, i + 2):
                            for y in range(j - 1, j + 2):
                                if not (0 <= x < 5 and 0 <= y < 5): continue
                                if (x, y) != (i, j) and state[x][y] == ' ':
                                    new_state = [r[:] for r in state]
                                    new_state[i][j] = ' '
                                    new_state[x][y] = color
                                    succ_list.append((new_state, (x, y), (i, j)))
        return succ_list

    # get heuristic game values
    def heuristic_game_value(self, state):
        points = 0
        def calculate_points(color, pos):
            scores = [0, 1, 4, 9, 16, 25]
            if color == ' ':
                return 0
            elif color == self.my_piece:
                return scores[pos]
            else:
                return -scores[pos]

        if self.game_value(state) != 0:
            return self.game_value(state)

        # Horizontal
        for row in state:
            points += self.table_points[''.join(row)]
        # Vertical 
        for col in range(len(state)):
            row = []
            for i in range(len(state)):
                row.append(state[i][col])
            points += self.table_points[''.join(row)]
         # \ Diagonal 
        r = [1, 0, 0]
        c = [0, 0, 1]
        for i in range(3):
            row = []
            for j in range(5):
                x = r[i] + j
                y = c[i] + j
                if not (0 <= x < 5 and 0 <= y < 5):
                    row.append(' ')
                    continue
                cell = state[x][y]
                row.append(cell)
            points += self.table_points[''.join(row)]
        # / Diagonal 
        r = [3, 4, 4]
        c = [0, 0, 1]
        for i in range(3):
            row = []
            for j in range(5):
                x = r[i] - j
                y = c[i] + j
                if not (0 <= x < 5 and 0 <= y < 5):
                    row.append(' ')
                    continue
                cell = state[x][y]
                row.append(cell)
            points += self.table_points[''.join(row)]
        # Diamond 
        for i in range(5):
            for j in range(5):
                if i-1>=0 and j-1>=0 and i<4 and j<4:
                    s = state[i-1][j] + state[i+1][j] + state[i][j -1] + state[i][j+1]
                    points += calculate_points(self.my_piece, s.count(self.my_piece))
                    points += calculate_points(self.opp, s.count(self.opp))
        return points / 100.0
############################################################################
#
# THE FOLLOWING CODE IS FOR SAMPLE GAMEPLAY ONLY
#
############################################################################
def main():
    print('Hello, this is Samaritan')
    ai = Teeko2Player()
    piece_count = 0
    turn = 0

    # drop phase
    while piece_count < 8 and ai.game_value(ai.board) == 0:

        # get the player or AI's move
        if ai.my_piece == ai.pieces[turn]:
            ai.print_board()
            move = ai.make_move(ai.board)
            ai.place_piece(move, ai.my_piece)
            print(ai.my_piece+" moved at "+chr(move[0][1]+ord("A"))+str(move[0][0]))
        else:
            move_made = False
            ai.print_board()
            print(ai.opp+"'s turn")
            while not move_made:
                player_move = input("Move (e.g. B3): ")
                while player_move[0] not in "ABCDE" or player_move[1] not in "01234":
                    player_move = input("Move (e.g. B3): ")
                try:
                    ai.opponent_move([(int(player_move[1]), ord(player_move[0])-ord("A"))])
                    move_made = True
                except Exception as e:
                    print(e)

        # update the game variables
        piece_count += 1
        turn += 1
        turn %= 2

    # move phase - can't have a winner until all 8 pieces are on the board
    while ai.game_value(ai.board) == 0:

        # get the player or AI's move
        if ai.my_piece == ai.pieces[turn]:
            ai.print_board()
            move = ai.make_move(ai.board)
            ai.place_piece(move, ai.my_piece)
            print(ai.my_piece+" moved from "+chr(move[1][1]+ord("A"))+str(move[1][0]))
            print("  to "+chr(move[0][1]+ord("A"))+str(move[0][0]))
        else:
            move_made = False
            ai.print_board()
            print(ai.opp+"'s turn")
            while not move_made:
                move_from = input("Move from (e.g. B3): ")
                while move_from[0] not in "ABCDE" or move_from[1] not in "01234":
                    move_from = input("Move from (e.g. B3): ")
                move_to = input("Move to (e.g. B3): ")
                while move_to[0] not in "ABCDE" or move_to[1] not in "01234":
                    move_to = input("Move to (e.g. B3): ")
                try:
                    ai.opponent_move([(int(move_to[1]), ord(move_to[0])-ord("A")),
                                    (int(move_from[1]), ord(move_from[0])-ord("A"))])
                    move_made = True
                except Exception as e:
                    print(e)

        # update the game variables
        turn += 1
        turn %= 2

    ai.print_board()
    if ai.game_value(ai.board) == 1:
        print("AI wins! Game over.")
    else:
        print("You win! Game over.")


if __name__ == "__main__":
    main()
